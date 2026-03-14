package com.flipkartclone.payments.exception;

import lombok.Getter;
@Getter
public enum ErrorCode {

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

    QUANTITY_REQUIRED(10011, "lineItem quantity is required"),
    QUANTITY_INVALID(10012, "lineItem quantity must be at least 1"),

    UNIT_AMOUNT_REQUIRED(10013, "lineItem price is required"),
    UNIT_AMOUNT_INVALID(10014, "lineItem price must be greater than 0"),

    CURRENCY_REQUIRED(10015, "currency is required"),
    CURRENCY_INVALID(10016, "currency must be a 3-letter ISO code");

    private final Integer errorCode;
    private final String errorMessage;

    ErrorCode(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}