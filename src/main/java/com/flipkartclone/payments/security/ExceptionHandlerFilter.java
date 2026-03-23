package com.flipkartclone.payments.security;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.pojo.ErrorResponse;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.util.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final JsonUtil jsonUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (PaymentValidationException ex) {
            log.error("Filter caught PaymentValidationException: code={}, msg={}",
                    ex.getError().getCode(), ex.getMessage());

            // Pass the FULL Exception object to handle dynamic messages/status
            sendErrorResponse(response, request, ex);
        } catch (Exception ex) {
            log.error("Filter caught Unhandled Exception: ", ex);

            // For generic exceptions, wrap them in a standard PaymentValidationException
            sendErrorResponse(response, request, new PaymentValidationException(ErrorCode.GENERIC_ERROR_CODE, ex.getMessage()));
        }
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   HttpServletRequest request,
                                   PaymentValidationException ex) throws IOException {

        ErrorCode errorCodeEnum = ex.getError();

        // Build the Response using our logic: Custom Message > Enum Message
        ErrorResponse errorDetail = ErrorResponse.builder()
                .message(ex.getCustomMessage() != null ? ex.getCustomMessage() : errorCodeEnum.getMessage())
                .errorCode(String.valueOf(errorCodeEnum.getCode()))
                .service(Constant.SERVICE_NAME)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().toString())
                .details("Security/Filter validation failed")
                .build();

        // Set Status: Custom Status > Enum Status
        int status = (ex.getCustomStatus() != null) ? ex.getCustomStatus().value() : errorCodeEnum.getHttpStatus().value();

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            // ObjectMapper use karke JSON write karein
            String json = jsonUtil.convertObjectToJson(errorDetail);
            response.getWriter().write(json);
        } catch (Exception jsonEx) {
            log.error("CRITICAL: Serialization failed in Filter", jsonEx);
            response.getWriter().write("{\"errorCode\":\"1000\",\"message\":\"Internal Server Error\"}");
        } finally {
            response.getWriter().flush();
        }
    }
}