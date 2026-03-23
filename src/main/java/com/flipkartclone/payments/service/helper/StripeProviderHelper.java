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
		HttpStatus status = (HttpStatus) httpResponse.getStatusCode();
		String body = httpResponse.getBody();

		// 1. Handle SUCCESS (2xx)
		if (status.is2xxSuccessful()) {

			SPPaymentResponse paymentResponse = jsonUtil.convertJsonToObject(body, SPPaymentResponse.class);

			if (paymentResponse != null  && paymentResponse.getCheckoutUrl() != null){
				log.info("Stripe API call successful. Status: {} | SessionID: {}",
						status, (paymentResponse.getSessionId() != null ? paymentResponse.getSessionId() : "N/A"));

				return paymentResponse;
			}

			// Case: 200 OK but body is empty or missing URL (Stripe's fault)
			log.error("Stripe returned 200 OK but required fields are missing. SessionID: {}, URL Present: {}",
					(paymentResponse != null ? paymentResponse.getSessionId() : "NULL"),
					(paymentResponse != null && paymentResponse.getCheckoutUrl() != null));

					throw new PaymentValidationException(
					ErrorCode.INVALID_PROVIDER_API_RESPONSE
			);
		}

		// 2. Handle ERRORS (4xx, 5xx)
		log.error("Stripe API failed. Status: {}, Body: {}", status, body);

		// Try to parse Stripe's specific error JSON
		SPErrorResponse stripeError = null;
		try {
			stripeError = jsonUtil.convertJsonToObject(body, SPErrorResponse.class);
		} catch (Exception e) {
			log.warn("Could not parse Stripe error JSON. Raw body: {}", body);
		}

		if (stripeError != null && stripeError.getErrorCode() != null) {
			// Log raw details for debugging
			log.error("Stripe Error Details | Code: {} | Message: {}",
					stripeError.getErrorCode(), stripeError.getErrorMessage());

			// Map Stripe's raw code to OUR standardized internal Enum
			var internalError = mapStripeToInternal(stripeError.getErrorCode());

			// Throwing with OUR Code but Stripe's specific Message for the user
			throw new PaymentValidationException(
					internalError,
					stripeError.getErrorMessage(),
					status
			);
		}

		// 3. FALLBACK: If no JSON error could be parsed
		throw new PaymentValidationException(
				ErrorCode.GENERIC_ERROR_CODE);
	}

	// Industry Standard Mapping logic
	private ErrorCode mapStripeToInternal(String stripeCode) {
		if (stripeCode == null) return ErrorCode.INVALID_PROVIDER_API_RESPONSE;

		return switch (stripeCode.toLowerCase()) {
			case "card_declined", "insufficient_funds" -> ErrorCode.PAYMENT_FAILED;
			case "expired_card" -> ErrorCode.CARD_EXPIRED;
			case "rate_limit" -> ErrorCode.TOO_MANY_REQUESTS;
			case "parameter_missing", "invalid_request_error" -> ErrorCode.BAD_REQUEST;
			default -> ErrorCode.INVALID_STRIPE_RESPONSE;
		};
	}
}