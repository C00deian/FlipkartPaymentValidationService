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

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentValidationException(
            PaymentValidationException ex,
            HttpServletRequest request) {

        ErrorCode errorCodeEnum = ex.getErrorCode();

        // Logic: Use dynamic message/status if provided, else use Enum defaults
        String finalMessage = (ex.getDynamicMessage() != null) ? ex.getDynamicMessage() : errorCodeEnum.getErrorMessage();
        HttpStatus finalStatus = (ex.getDynamicStatus() != null) ? ex.getDynamicStatus() : errorCodeEnum.getHttpStatus();

        ErrorResponse errorResponse = buildError(
                finalMessage,
                String.valueOf(errorCodeEnum.getErrorCode()),
                ex.getDetails(),
                request
        );

        log.error("Payment Exception | status: {} | message: {} | errorCode: {} | details: {}",
                finalStatus, finalMessage, errorCodeEnum.getErrorCode(), ex.getDetails());

        return ResponseEntity
                .status(finalStatus)
                .body(errorResponse);
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
                errorCodeEnum.getErrorMessage(),
                String.valueOf(errorCodeEnum.getErrorCode()),
                enumKey,
                request
        );

        log.error("Jakarta validation failed | message: {} | errorCode: {} | key: {}",
                errorCodeEnum.getErrorMessage(),
                errorCodeEnum.getErrorCode(),
                enumKey);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(
            Exception ex,
            HttpServletRequest request) {
        ErrorResponse errorResponse = buildError(
                ErrorCode.GENERIC_ERROR_CODE.getErrorMessage(),
                String.valueOf(ErrorCode.GENERIC_ERROR_CODE.getErrorCode()),
                ex.getMessage(),
                request
        );
        log.error("Unhandled exception in payment validation flow", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}