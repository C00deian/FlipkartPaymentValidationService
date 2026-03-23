package com.flipkartclone.payments.exception;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.pojo.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalPaymentValidationExceptionHandler {

    // Helper method to build a consistent response
    private ErrorResponse buildError(String message, String errorCode, String details, HttpServletRequest request) {
        return ErrorResponse.builder()
                .message(message)
                .errorCode(errorCode)
                .service(Constant.SERVICE_NAME)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().toString())
                .details(details)
                .build();
    }

    // 1. Handle Custom Payment Exceptions (Internal + Provider Errors)
    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentValidationException(
            PaymentValidationException ex,
            HttpServletRequest request) {

        ErrorCode errorEnum = ex.getError();

        // Priority: CustomMessage > EnumMessage
        String finalMessage = (ex.getCustomMessage() != null) ? ex.getCustomMessage() : errorEnum.getMessage();
        // Priority: CustomStatus > EnumStatus
        HttpStatus finalStatus = (ex.getCustomStatus() != null) ? ex.getCustomStatus() : errorEnum.getHttpStatus();

        ErrorResponse errorResponse = buildError(
                finalMessage,
                String.valueOf(errorEnum.getCode()),
                "Payment validation failed",
                request
        );

        log.error("Payment Exception | Status: {} | Code: {} | Msg: {}",
                finalStatus, errorEnum.getCode(), finalMessage);

        return ResponseEntity.status(finalStatus).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .getFirst();

        String enumKey = fieldError != null ? fieldError.getDefaultMessage() : null;

        ErrorCode errorCodeEnum;
        try {
            errorCodeEnum = ErrorCode.valueOf(enumKey);
        } catch (Exception ignored) {
            errorCodeEnum = ErrorCode.GENERIC_ERROR_CODE;
        }

        ErrorResponse errorResponse = buildError(
                errorCodeEnum.getMessage(),
                String.valueOf(errorCodeEnum.getCode()),
                enumKey,
                request
        );

        log.error("Jakarta validation failed | message: {} | errorCode: {} | key: {}",
                errorCodeEnum.getMessage(),
                errorCodeEnum.getCode(),
                enumKey);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 3. Catch-all for any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(
            Exception ex,
            HttpServletRequest request) {
        ErrorResponse errorResponse = buildError(
                ErrorCode.GENERIC_ERROR_CODE.getMessage(),
                String.valueOf(ErrorCode.GENERIC_ERROR_CODE.getCode()),
                ex.getMessage(),
                request
        );

        log.error("Unhandled Exception caught in Payment Service", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}