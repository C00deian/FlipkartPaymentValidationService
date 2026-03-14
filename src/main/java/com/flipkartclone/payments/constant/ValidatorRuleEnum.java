package com.flipkartclone.payments.constant;

import com.flipkartclone.payments.service.Impl.ValidatorRule1;
import com.flipkartclone.payments.service.Impl.ValidatorRule2;
import com.flipkartclone.payments.service.Impl.ValidatorRule3;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ValidatorRuleEnum {

    VALIDATE_RULE1("VALIDATE_RULE1", ValidatorRule1.class),
    VALIDATE_RULE2("VALIDATE_RULE2", ValidatorRule2.class),
    VALIDATE_RULE3("VALIDATE_RULE3", ValidatorRule3.class);

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

