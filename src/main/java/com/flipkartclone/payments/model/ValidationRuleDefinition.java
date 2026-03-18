package com.flipkartclone.payments.model;

import java.util.Map;

public record ValidationRuleDefinition(String validatorName, short priority, Map<String, String> params) {

    public ValidationRuleDefinition {
        params = params == null ? Map.of() : Map.copyOf(params);
    }
}
