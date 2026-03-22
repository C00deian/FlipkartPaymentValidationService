package com.flipkartclone.payments.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.PaymentRequest;
import com.flipkartclone.payments.stripeprovider.LineItem;
import com.flipkartclone.payments.stripeprovider.SPCreatePaymentReq;
import com.flipkartclone.payments.stripeprovider.SPErrorResponse;
import com.flipkartclone.payments.stripeprovider.SPPaymentResponse;
import com.flipkartclone.payments.util.JsonUtil;
import com.flipkartclone.payments.http.HttpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeProviderHelper {

	private final JsonUtil jsonUtil;

	@Value("${stripe.provider.createPaymentUrl}")
	private String createStripeProviderPaymentUrl;

	public HttpRequest createHttpRequest(PaymentRequest paymentRequest) {
		log.info("Creating HttpRequest from PaymentRequest: {}", paymentRequest);

		SPCreatePaymentReq spReq = new SPCreatePaymentReq();
		spReq.setSuccessUrl(paymentRequest.getPayment().getSuccessUrl());
		spReq.setCancelUrl(paymentRequest.getPayment().getCancelUrl());

		if (paymentRequest.getPayment().getLineItems() != null
				&& !paymentRequest.getPayment().getLineItems().isEmpty()) {
			List<LineItem> spLineItems = paymentRequest.getPayment()
					.getLineItems()
					.stream()
					.map(li -> {
						LineItem item = new LineItem();
						item.setCurrency(li.getCurrency());
						item.setProductName(li.getProductName());
						item.setUnitAmount(li.getUnitAmount());
						item.setQuantity(li.getQuantity() == null ? 0 : li.getQuantity());
						return item;
					})
					.collect(Collectors.toList());

			spReq.setLineItems(spLineItems);
		}

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpHeaders(new HttpHeaders());
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setUrl(createStripeProviderPaymentUrl);
		httpRequest.setRequestData(spReq);

		return httpRequest;
	}

	public SPPaymentResponse processResponse(ResponseEntity<String> httpResponse) {

		// 1. HANDLE SUCCESS (2xx)
		if (httpResponse.getStatusCode().is2xxSuccessful()) {
			SPPaymentResponse paymentResponse = jsonUtil.convertJsonToObject(
					httpResponse.getBody(), SPPaymentResponse.class);

			if (paymentResponse != null && paymentResponse.getCheckoutUrl() != null) {
				log.info("Stripe API call successful. URL: {}", paymentResponse.getCheckoutUrl());
				return paymentResponse;
			}

			log.error("Stripe returned 2xx but body is invalid or missing URL: {}", httpResponse.getBody());
			throw new PaymentValidationException(
					ErrorCode.INVALID_STRIPE_PROVIDER_RESPONSE,
					"Stripe response was empty or missing hosted page URL"
			);
		}

		// 2. HANDLE CLIENT/SERVER ERRORS (4xx, 5xx)
		log.error("Stripe API call failed. Status: {}, Body: {}",
				httpResponse.getStatusCode(), httpResponse.getBody());

		try {
			SPErrorResponse stripeError = jsonUtil.convertJsonToObject(
					httpResponse.getBody(), SPErrorResponse.class);

			if (stripeError != null) {
				// Throw dynamic exception with Stripe's specific message and status
				throw new PaymentValidationException(
						stripeError.getErrorMessage(),
						"Stripe-Error-Code: " + stripeError.getErrorCode(),
						(HttpStatus) httpResponse.getStatusCode()
				);
			}
		} catch (Exception e) {
			log.error("Failed to parse Stripe error response as JSON", e);
		}

		// 3. GENERIC FALLBACK (Network issues / Unreadable responses)
		throw new PaymentValidationException(
				ErrorCode.ERROR_CONNECTING_TO_EXTERNAL_SERVICE,
				"Unexpected status received: " + httpResponse.getStatusCode()
		);
	}
}