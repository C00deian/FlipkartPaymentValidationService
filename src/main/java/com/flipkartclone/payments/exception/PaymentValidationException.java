package com.flipkartclone.payments.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentValidationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String details;
    private final String dynamicMessage; // To capture Stripe's specific message
    private final HttpStatus dynamicStatus; // To capture Stripe's specific status

    // Standard constructor for your predefined Enums
    public PaymentValidationException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public PaymentValidationException(ErrorCode errorCode, String details) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.details = details;
        this.dynamicMessage = null;
        this.dynamicStatus = null;
    }

    // Constructor for DYNAMIC errors (External Provider errors)
    public PaymentValidationException(String dynamicMessage, String details, HttpStatus status) {
        super(dynamicMessage);
        this.errorCode = ErrorCode.INVALID_API_RESPONSE; // Use this as the base category
        this.details = details;
        this.dynamicMessage = dynamicMessage;
        this.dynamicStatus = status;
    }
}