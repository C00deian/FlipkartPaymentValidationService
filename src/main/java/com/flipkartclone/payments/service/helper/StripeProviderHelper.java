package com.flipkartclone.payments.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.Payment;
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

	public HttpRequest createHttpRequest(Payment paymentRequest) {
		log.info("Mapping request for provider. OrderId: {}", paymentRequest.getMerchantTxnRef());

		// 1. lineItem Mapping
		List<LineItem> spLineItems = mapToProviderLineItems(paymentRequest.getLineItems());

		// 2. prepare Payload for Stripe-provider-service SPCreatePaymentReq
		SPCreatePaymentReq spReq = buildProviderRequest(paymentRequest, spLineItems);
		log.info("orderId  {}:" , spReq.getOrderId());

		// 3. final Http request.
		return buildFinalHttpRequest(spReq);
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
					(paymentResponse != null ? paymentResponse.getSessionId() : "NULL"), "No Url Present");

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



	// --- EXTRACTED HELPER METHODS ---
	private List<LineItem> mapToProviderLineItems(List<com.flipkartclone.payments.pojo.LineItem> sourceItems) {
		if (sourceItems == null || sourceItems.isEmpty()) {
			return List.of();
		}

		return sourceItems.stream()
				.map(li -> LineItem.builder()
						.currency(li.getCurrency())
						.productName(li.getProductName())
						.unitAmount(li.getUnitAmount())
						.quantity(li.getQuantity() != null ? li.getQuantity() : 0)
						.build())
				.collect(Collectors.toList());
	}

	private SPCreatePaymentReq buildProviderRequest(Payment payment, List<LineItem> spLineItems) {
		return SPCreatePaymentReq.builder()
				.orderId(payment.getMerchantTxnRef())
				.amount(payment.getAmount())
				.currency(payment.getCurrency())
				.merchantTxnRef(payment.getMerchantTxnRef())
				.userId(payment.getEndUserID())
				.successUrl(payment.getSuccessUrl())
				.cancelUrl(payment.getCancelUrl())
				.lineItems(spLineItems)
				.build();
	}

	private HttpRequest buildFinalHttpRequest(SPCreatePaymentReq spReq) {
		return HttpRequest.builder()
				.httpHeaders(new HttpHeaders()) // Add custom headers here if needed
				.httpMethod(HttpMethod.POST)
				.url(createStripeProviderPaymentUrl)
				.requestData(spReq)
				.build();
	}

}