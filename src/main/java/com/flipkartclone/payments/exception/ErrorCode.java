package com.flipkartclone.payments.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // --- 10xx: Generic & Server Errors ---
    GENERIC_ERROR_CODE(1000, "An unexpected error occurred while processing the payment request.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_SAVE_FAILED(1001, "Failed to save payment request. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_VALIDATION_RULES_CONFIGURED(1002, "No validation rules are configured.", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_SERVICE_CONNECTION_ERROR(1003, "Error connecting to external service.", HttpStatus.INTERNAL_SERVER_ERROR),
    HMAC_COMPUTATION_FAILED(1004, "HMAC computation failed. Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR),
    JSON_CONVERSION_ERROR(1006, "Failed to process data format.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST_PAYLOAD(1007, "The request payload is invalid or corrupt.", HttpStatus.BAD_REQUEST),

  // --- 11xx: URL Related Errors ---
    SUCCESS_URL_MISSING(1100, "Success URL is missing.", HttpStatus.BAD_REQUEST),
    SUCCESS_URL_TOO_LONG(1101, "Success URL cannot exceed 500 characters.", HttpStatus.BAD_REQUEST),
    SUCCESS_URL_INVALID(1102, "Success URL must be a valid http or https URL.", HttpStatus.BAD_REQUEST),
    CANCEL_URL_MISSING(1103, "Cancel URL is missing.", HttpStatus.BAD_REQUEST),
    CANCEL_URL_TOO_LONG(1104, "Cancel URL cannot exceed 500 characters.", HttpStatus.BAD_REQUEST),
    CANCEL_URL_INVALID(1105, "Cancel URL must be a valid http or https URL.", HttpStatus.BAD_REQUEST),

    // --- 12xx: Line Items & Product Errors ---
    LINE_ITEMS_NULL(1200, "Line items cannot be null.", HttpStatus.BAD_REQUEST),
    LINE_ITEMS_EMPTY(1201, "Line items cannot be empty.", HttpStatus.BAD_REQUEST),
    LINE_ITEMS_LIMIT_EXCEEDED(1202, "Maximum 50 line items allowed.", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_MISSING(1203, "Line item name is required.", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_TOO_LONG(1204, "Line item name cannot exceed 200 characters.", HttpStatus.BAD_REQUEST),
    QUANTITY_REQUIRED(1205, "Line item quantity is required.", HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(1206, "Line item quantity must be at least 1.", HttpStatus.BAD_REQUEST),
    UNIT_AMOUNT_REQUIRED(1207, "Line item price is required.", HttpStatus.BAD_REQUEST),
    UNIT_AMOUNT_INVALID(1208, "Line item price must be greater than 0.", HttpStatus.BAD_REQUEST),

    // --- 13xx: Transaction & Currency Errors ---
    CURRENCY_REQUIRED(1300, "Currency is required.", HttpStatus.BAD_REQUEST),
    CURRENCY_INVALID(1301, "Currency must be a 3-letter ISO code.", HttpStatus.BAD_REQUEST),
    AMOUNT_REQUIRED(1302, "Amount is required.", HttpStatus.BAD_REQUEST),
    AMOUNT_INVALID(1303, "Amount must be greater than 0.", HttpStatus.BAD_REQUEST),
    MERCHANT_TXN_REF_REQUIRED(1304, "Merchant transaction reference is required.", HttpStatus.BAD_REQUEST),
    MERCHANT_TXN_REF_TOO_LONG(1305, "Merchant transaction reference cannot exceed 100 characters.", HttpStatus.BAD_REQUEST),
    DUPLICATE_MERCHANT_TXN_REF(1306, "Merchant transaction reference must be unique. Duplicate found.", HttpStatus.CONFLICT),
    DUPLICATE_TRANSACTION(1307, "Duplicate transaction detected.", HttpStatus.CONFLICT),

    // --- 14xx: User & Customer Info ---
    USER_ID_REQUIRED(1400, "End user ID is required.", HttpStatus.BAD_REQUEST),
    FIRST_NAME_REQUIRED(1401, "First name is required.", HttpStatus.BAD_REQUEST),
    LAST_NAME_REQUIRED(1402, "Last name is required.", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(1403, "Email is required.", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1404, "Email must be valid.", HttpStatus.BAD_REQUEST),
    MOBILE_PHONE_REQUIRED(1405, "Mobile phone is required.", HttpStatus.BAD_REQUEST),
    MOBILE_PHONE_INVALID(1406, "Mobile phone format is invalid.", HttpStatus.BAD_REQUEST),

    // --- 15xx: Security & Authentication ---
    MISSING_HMAC_SIGNATURE(1500, "Missing HMAC signature in request headers.", HttpStatus.UNAUTHORIZED),
    INVALID_HMAC_SIGNATURE(1501, "Invalid HMAC signature. Authentication failed.", HttpStatus.UNAUTHORIZED),
    PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED(1502, "Payment attempt threshold exceeded. Please try again later.", HttpStatus.TOO_MANY_REQUESTS),

    // --- 16xx: Provider & Rules ---
    PAYMENT_METHOD_REQUIRED(1600, "Payment method is required.", HttpStatus.BAD_REQUEST),
    PROVIDER_REQUIRED(1601, "Provider is required.", HttpStatus.BAD_REQUEST),
    PAYMENT_TYPE_REQUIRED(1602, "Payment type is required.", HttpStatus.BAD_REQUEST),
    VALIDATION_RULE_FAILED(1603, "Business validation rule failed.", HttpStatus.BAD_REQUEST),
    INVALID_STRIPE_RESPONSE(1604, "Stripe response was empty or invalid.", HttpStatus.BAD_GATEWAY),
    INVALID_PROVIDER_API_RESPONSE(1605, "Invalid response received from payment service provider.", HttpStatus.BAD_GATEWAY),


    // --- 17xx: Stripe/Provider Specific Errors (Mapped) ---
    PAYMENT_FAILED(1700, "The payment was declined by the provider.", HttpStatus.PAYMENT_REQUIRED),
    CARD_EXPIRED(1701, "The provided card has expired.", HttpStatus.BAD_REQUEST),
    TOO_MANY_REQUESTS(1702, "Too many requests to the payment provider.", HttpStatus.TOO_MANY_REQUESTS),
    BAD_REQUEST(1703, "Invalid request sent to payment provider.", HttpStatus.BAD_REQUEST);



    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}