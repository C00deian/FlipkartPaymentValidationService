package com.flipkartclone.payments.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS_URL_INVALID("30010", "Success URL is not a valid http/https URL"),
    CANCEL_URL_INVALID("30011", "Cancel URL is not a valid http/https URL"),
    INVALID_CURRENCY("30001", "The specified currency is not supported or invalid"),
    INVALID_AMOUNT("30002", "The specified amount is invalid (e.g., negative or zero)"),
    ERROR_CONNECTING_TO_EXTERNAL_SERVICE("30012", "Error connecting to external payment service"),
    STRIPE_API_ERROR("30013", "<Dynamically prepared error message based on Stripe API response>"),
    INVALID_REQUEST("30003", "The request is invalid (e.g., missing required parameters, invalid data types)"),
    GENERIC_ERROR("30000", "An unexpected error occurred"),
    INVALID_API_RESPONSE("30014", "Received an invalid response from the external payment service");

    private final String errorCode;
    private final String errorMessage;

    ErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}