package com.flipkartclone.payments.exception;

import lombok.Getter;
@Getter
public enum ErrorCode {

    GENERIC_ERROR_CODE(10000, "An Unexpected Error Occurred while processing the payment request. Please try again later."),
    SUCCESS_URL_MISSING(10001, "success URL is missing."),
    SUCCESS_URL_TOO_LONG(10002, "success URL cannot exceed 500 characters"),
    SUCCESS_URL_INVALID(10003, "success URL must be a valid http or https URL"),

    CANCEL_URL_MISSING(10004, "cancel URL is missing."),
    CANCEL_URL_TOO_LONG(10005, "cancel URL cannot exceed 500 characters"),
    CANCEL_URL_INVALID(10006, "cancel URL must be a valid http or https URL"),

    LINE_ITEMS_NULL(10007, "lineItems cannot be null"),
    LINE_ITEMS_EMPTY(10008, "lineItems cannot be empty"),
    LINE_ITEMS_LIMIT_EXCEEDED(10009, "Maximum 50 lineItems allowed"),

    PRODUCT_NAME_MISSING(10010, "lineItem name is required"),
    PRODUCT_NAME_TOO_LONG(10034, "lineItem name cannot exceed 200 characters"),

    QUANTITY_REQUIRED(10011, "lineItem quantity is required"),
    QUANTITY_INVALID(10012, "lineItem quantity must be at least 1"),

    UNIT_AMOUNT_REQUIRED(10013, "lineItem price is required"),
    UNIT_AMOUNT_INVALID(10014, "lineItem price must be greater than 0"),

    CURRENCY_REQUIRED(10015, "currency is required"),
    CURRENCY_INVALID(10016, "currency must be a 3-letter ISO code"),

    // ---------------- NEW USER VALIDATIONS ----------------

    USER_ID_REQUIRED(10017, "endUserID is required"),
    FIRST_NAME_REQUIRED(10018, "firstname is required"),
    LAST_NAME_REQUIRED(10019, "lastname is required"),
    EMAIL_REQUIRED(10020, "email is required"),
    EMAIL_INVALID(10021, "email must be valid"),
    MOBILE_PHONE_REQUIRED(10022, "mobilePhone is required"),
    MOBILE_PHONE_INVALID(10023, "mobilePhone format is invalid"),

    // ---------------- NEW PAYMENT VALIDATIONS ----------------

    AMOUNT_REQUIRED(10024, "amount is required"),
    AMOUNT_INVALID(10025, "amount must be greater than 0"),

    BRAND_NAME_REQUIRED(10026, "brandName is required"),
    LOCALE_REQUIRED(10027, "locale is required"),
    COUNTRY_REQUIRED(10028, "country is required"),

    MERCHANT_TXN_REF_REQUIRED(10029, "merchantTxnRef is required"),
    MERCHANT_TXN_REF_TOO_LONG(10030, "merchantTxnRef cannot exceed 100 characters"),
    DUPLICATE_MERCHANT_TXN_REF(10038, "merchantTxnRef must be unique. Duplicate reference found."),
    DUPLICATE_TRANSACTION(10040, "Duplicate transaction detected."),
    PAYMENT_METHOD_REQUIRED(10031, "paymentMethod is required"),
    PROVIDER_REQUIRED(10032, "provider is required"),
    PAYMENT_TYPE_REQUIRED(10033, "paymentType is required"),

    VALIDATOR_RULE1_FAILED(10035, "Rule1 business validation failed"),
    VALIDATOR_RULE2_FAILED(10036, "Rule2 business validation failed"),
    VALIDATOR_RULE3_FAILED(10037, "Rule3 business validation failed"),
    INVALID_API_RESPONSE(100039, "Invalid response received from payment service provider"),
    FAILED_TO_SAVE_PAYMENT_REQUEST(10041, "Failed to save payment request. Please try again later.");

    private final Integer errorCode;
    private final String errorMessage;

    ErrorCode(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}