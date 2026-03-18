package com.flipkartclone.payments.security;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.dto.ErrorResponseDto;
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
                                    HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            log.info("ExceptionHandlerFilter: Entering Filter Chain");
            filterChain.doFilter(request, response);
            log.info("ExceptionHandlerFilter: Filter Chain Processed Successfully");
        } catch (PaymentValidationException ex) {
            log.error("Filter caught PaymentValidationException: {}", ex.getMessage());
            sendErrorResponse(response, request, ex.getErrorCode(), ex.getDetails());
        } catch (Exception ex) {
            log.error("Filter caught Unhandled Exception: ", ex);
            sendErrorResponse(response, request, ErrorCode.GENERIC_ERROR_CODE, ex.getMessage());
        }
    }


    private void sendErrorResponse(HttpServletResponse response,
                                   HttpServletRequest request,
                                   ErrorCode errorCode,
                                   String details) throws IOException {

        // Build the DTO dynamically from the ErrorCode enum
        ErrorResponseDto errorDetail = ErrorResponseDto.builder()
                .message(errorCode.getErrorMessage())
                .errorCode(String.valueOf(errorCode.getErrorCode()))
                .service(Constant.SERVICE_NAME)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().toString())
                .details(details)
                .build();

        // Use the HTTP Status directly from your Enum
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            String json = jsonUtil.convertObjectToJson(errorDetail);
            response.getWriter().write(json);
            log.info("Error response written to body for path: {}", request.getRequestURI());
        } catch (Exception jsonEx) {
            log.error("CRITICAL: JsonUtil failed even in Filter, sending fallback string", jsonEx);
            response.getWriter().write("{\"message\":\"" + errorCode.getErrorMessage() + "\"}");
        } finally {
            response.getWriter().flush();
        }
    }
}