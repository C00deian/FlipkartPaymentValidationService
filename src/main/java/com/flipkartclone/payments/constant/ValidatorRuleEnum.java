package com.flipkartclone.payments.constant;

import com.flipkartclone.payments.service.Impl.DuplicateTxnValidator;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ValidatorRuleEnum {

    VALIDATE_RULE1("DUPLICATE_TXN_RULE", DuplicateTxnValidator.class);

    private final String ruleName;
    private final Class<? extends BusinessValidator> validatorClass;

    ValidatorRuleEnum(String ruleName, Class<? extends BusinessValidator> validatorClass) {
        this.ruleName = ruleName;
        this.validatorClass = validatorClass;
    }

    public static Class<? extends BusinessValidator> getValidatorClassByRule(String ruleName) {
        if (ruleName == null || ruleName.isBlank()) {
            throw new IllegalArgumentException("Rule name cannot be null or blank");
        }

        return Arrays.stream(values())
                .filter(rule -> rule.getRuleName().equalsIgnoreCase(ruleName.trim()))
                .findFirst()
                .map(ValidatorRuleEnum::getValidatorClass)
                .orElseThrow(() -> new IllegalArgumentException("Unknown validator rule: " + ruleName));
    }
}

