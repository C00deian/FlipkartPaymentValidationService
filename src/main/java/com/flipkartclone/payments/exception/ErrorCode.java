package com.flipkartclone.payments.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    GENERIC_ERROR_CODE(10000, "An Unexpected Error Occurred while processing the payment request. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    SUCCESS_URL_MISSING(10001, "success URL is missing.", HttpStatus.BAD_REQUEST),
    SUCCESS_URL_TOO_LONG(10002, "success URL cannot exceed 500 characters", HttpStatus.BAD_REQUEST),
    SUCCESS_URL_INVALID(10003, "success URL must be a valid http or https URL", HttpStatus.BAD_REQUEST),
    CANCEL_URL_MISSING(10004, "cancel URL is missing.", HttpStatus.BAD_REQUEST),
    CANCEL_URL_TOO_LONG(10005, "cancel URL cannot exceed 500 characters", HttpStatus.BAD_REQUEST),
    CANCEL_URL_INVALID(10006, "cancel URL must be a valid http or https URL", HttpStatus.BAD_REQUEST),
    LINE_ITEMS_NULL(10007, "lineItems cannot be null", HttpStatus.BAD_REQUEST),
    LINE_ITEMS_EMPTY(10008, "lineItems cannot be empty", HttpStatus.BAD_REQUEST),
    LINE_ITEMS_LIMIT_EXCEEDED(10009, "Maximum 50 lineItems allowed", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_MISSING(10010, "lineItem name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_TOO_LONG(10034, "lineItem name cannot exceed 200 characters", HttpStatus.BAD_REQUEST),
    QUANTITY_REQUIRED(10011, "lineItem quantity is required", HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(10012, "lineItem quantity must be at least 1", HttpStatus.BAD_REQUEST),
    UNIT_AMOUNT_REQUIRED(10013, "lineItem price is required", HttpStatus.BAD_REQUEST),
    UNIT_AMOUNT_INVALID(10014, "lineItem price must be greater than 0", HttpStatus.BAD_REQUEST),
    CURRENCY_REQUIRED(10015, "currency is required", HttpStatus.BAD_REQUEST),
    CURRENCY_INVALID(10016, "currency must be a 3-letter ISO code", HttpStatus.BAD_REQUEST),
    USER_ID_REQUIRED(10017, "endUserID is required", HttpStatus.BAD_REQUEST),
    FIRST_NAME_REQUIRED(10018, "firstname is required", HttpStatus.BAD_REQUEST),
    LAST_NAME_REQUIRED(10019, "lastname is required", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(10020, "email is required", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(10021, "email must be valid", HttpStatus.BAD_REQUEST),
    MOBILE_PHONE_REQUIRED(10022, "mobilePhone is required", HttpStatus.BAD_REQUEST),
    MOBILE_PHONE_INVALID(10023, "mobilePhone format is invalid", HttpStatus.BAD_REQUEST),
    AMOUNT_REQUIRED(10024, "amount is required", HttpStatus.BAD_REQUEST),
    AMOUNT_INVALID(10025, "amount must be greater than 0", HttpStatus.BAD_REQUEST),
    BRAND_NAME_REQUIRED(10026, "brandName is required", HttpStatus.BAD_REQUEST),
    LOCALE_REQUIRED(10027, "locale is required", HttpStatus.BAD_REQUEST),
    COUNTRY_REQUIRED(10028, "country is required", HttpStatus.BAD_REQUEST),
    MERCHANT_TXN_REF_REQUIRED(10029, "merchantTxnRef is required", HttpStatus.BAD_REQUEST),
    MERCHANT_TXN_REF_TOO_LONG(10030, "merchantTxnRef cannot exceed 100 characters", HttpStatus.BAD_REQUEST),
    DUPLICATE_MERCHANT_TXN_REF(10038, "merchantTxnRef must be unique. Duplicate reference found.", HttpStatus.CONFLICT),
    DUPLICATE_TRANSACTION(10040, "Duplicate transaction detected.", HttpStatus.CONFLICT),
    PAYMENT_METHOD_REQUIRED(10031, "paymentMethod is required", HttpStatus.BAD_REQUEST),
    PROVIDER_REQUIRED(10032, "provider is required", HttpStatus.BAD_REQUEST),
    PAYMENT_TYPE_REQUIRED(10033, "paymentType is required", HttpStatus.BAD_REQUEST),
    VALIDATOR_RULE1_FAILED(10035, "Duplicate TXn Validation Failed", HttpStatus.BAD_REQUEST),
    VALIDATOR_RULE2_FAILED(10036, "Rule2 business validation failed", HttpStatus.BAD_REQUEST),
    VALIDATOR_RULE3_FAILED(10037, "Rule3 business validation failed", HttpStatus.BAD_REQUEST),
    INVALID_API_RESPONSE(10039, "Invalid response received from payment service provider", HttpStatus.BAD_GATEWAY),
    FAILED_TO_SAVE_PAYMENT_REQUEST(10041, "Failed to save payment request. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    MISSING_HMAC_SIGNATURE(10042, "Missing HMAC signature in request headers.", HttpStatus.UNAUTHORIZED),
    INVALID_HMAC_SIGNATURE(10043, "Invalid HMAC signature. Authentication failed.", HttpStatus.UNAUTHORIZED),
    HMAC_COMPUTATION_FAILED(10044, "HMAC Computation failed! Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    private final Integer errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    ErrorCode(Integer errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}