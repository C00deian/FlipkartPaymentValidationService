package com.flipkartclone.payments.model;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;

import java.util.Map;

public record ValidationContext(String validatorName, short priority, Map<String, String> params) {

    public ValidationContext {
        params = params == null ? Map.of() : Map.copyOf(params);
    }

    public String getRequiredParam(String paramName) {
        String paramValue = params.get(paramName);
        if (paramValue == null || paramValue.isBlank()) {
            throw new PaymentValidationException(
                    ErrorCode.INVALID_VALIDATION_RULE_CONFIGURATION,
                    "Missing required param '%s' for validator '%s'".formatted(paramName, validatorName)
            );
        }

        return paramValue.trim();
    }

    public int getRequiredInt(String paramName) {
        String rawValue = getRequiredParam(paramName);
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException ex) {
            throw new PaymentValidationException(
                    ErrorCode.INVALID_VALIDATION_RULE_CONFIGURATION,
                    "Invalid integer value '%s' for param '%s' in validator '%s'"
                            .formatted(rawValue, paramName, validatorName)
            );
        }
    }
}
