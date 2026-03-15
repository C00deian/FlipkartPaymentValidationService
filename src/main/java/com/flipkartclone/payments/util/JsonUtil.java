package com.flipkartclone.payments.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {

        if (json == null || json.isBlank()) {
            log.error("JSON payload is empty for class {}", clazz.getSimpleName());

            throw new PaymentValidationException(
                    ErrorCode.INVALID_API_RESPONSE,
                    ErrorCode.INVALID_API_RESPONSE.getErrorMessage()
            );
        }

        try {
            return mapper.readValue(json, clazz);

        } catch (Exception e) {

            log.error("JSON conversion failed for class {}", clazz.getSimpleName(), e);

            throw new PaymentValidationException(
                    ErrorCode.INVALID_API_RESPONSE,
                   ErrorCode.INVALID_API_RESPONSE.getErrorMessage()
            );
        }
    }

    public static String convertObjectToJson(Object object) {

        if (object == null) {

            log.error("Object payload is null");

            throw new PaymentValidationException(
                    ErrorCode.INVALID_API_RESPONSE,
                   ErrorCode.INVALID_API_RESPONSE.getErrorMessage()
            );
        }

        try {

            return mapper.writeValueAsString(object);

        } catch (Exception e) {

            log.error("Object conversion to JSON failed", e);

            throw new PaymentValidationException(
                    ErrorCode.INVALID_API_RESPONSE,
                     ErrorCode.INVALID_API_RESPONSE.getErrorMessage()
            );
        }
    }
}