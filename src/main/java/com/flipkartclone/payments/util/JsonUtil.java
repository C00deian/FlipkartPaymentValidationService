package com.flipkartclone.payments.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public <T> T convertJsonToObject(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) {
            log.error("JSON payload is empty for class {}", clazz.getSimpleName());
            throw new PaymentValidationException(ErrorCode.INVALID_PROVIDER_API_RESPONSE);
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("JSON mapping failed for class {}: {}", clazz.getSimpleName(), e.getMessage());
            // Ye technical failure hai
            throw new PaymentValidationException(
                    ErrorCode.JSON_CONVERSION_ERROR,
                    "Error parsing " + clazz.getSimpleName() + " from JSON"
            );
        }
    }

    public String convertObjectToJson(Object object) {
        if (object == null) {
            log.error("Object to convert is null");
            throw new PaymentValidationException(ErrorCode.GENERIC_ERROR_CODE);
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Serialization failed: {}", e.getMessage());
            throw new PaymentValidationException(ErrorCode.JSON_CONVERSION_ERROR);
        }
    }

}