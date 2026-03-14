package com.flipkartclone.payments.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentValidationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;

}