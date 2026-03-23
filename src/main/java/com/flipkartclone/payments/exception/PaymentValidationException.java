package com.flipkartclone.payments.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentValidationException extends RuntimeException {

    private final ErrorCode error;
    private final String customMessage;
    private final HttpStatus customStatus;

    // 1. Standard Constructor use Enum as it is.
    public PaymentValidationException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
        this.customMessage = error.getMessage();
        this.customStatus = error.getHttpStatus();
    }

    // 2. Dynamic Message Constructor: Enum + Stripe ka message
    public PaymentValidationException(ErrorCode error, String dynamicMessage) {
        super(dynamicMessage);
        this.error = error;
        this.customMessage = dynamicMessage;
        this.customStatus = error.getHttpStatus();
    }

    // 3. Full Dynamic Constructor: Enum + Stripe ka message + Custom Status (e.g., 502 for Stripe failure)
    public PaymentValidationException(ErrorCode error, String dynamicMessage, HttpStatus dynamicStatus) {
        super(dynamicMessage);
        this.error = error;
        this.customMessage = dynamicMessage;
        this.customStatus = dynamicStatus;
    }
}