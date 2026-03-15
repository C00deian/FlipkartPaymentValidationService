package com.flipkartclone.payments.exception;

import lombok.Getter;

@Getter
public class PaymentValidationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;

    public PaymentValidationException(ErrorCode errorCode, String details) {
        super(details);
        this.errorCode = errorCode;
        this.details = details;
    }
}