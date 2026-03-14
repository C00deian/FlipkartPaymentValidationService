package com.flipkartclone.payments.exception;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.dto.ErrorResponseDto;
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

    private ErrorResponseDto buildError(String message, String errorCode, HttpServletRequest request) {
        return ErrorResponseDto.builder()
                .message(message)
                .errorCode(errorCode)
                .service(Constant.SERVICE_NAME)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .getFirst();

        String enumKey = fieldError.getDefaultMessage();

        ErrorCode errorCodeEnum = ErrorCode.valueOf(enumKey);
        String errorMessage = errorCodeEnum.getErrorMessage();

        ErrorResponseDto errorResponse = buildError(
                errorMessage,
                String.valueOf(errorCodeEnum.getErrorCode()),
                request
        );

        log.error("Validation failed | message: {} | errorCode: {}",
                errorMessage,
                errorCodeEnum.getErrorCode());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}