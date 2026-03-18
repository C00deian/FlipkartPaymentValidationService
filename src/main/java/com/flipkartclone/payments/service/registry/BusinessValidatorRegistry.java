package com.flipkartclone.payments.service.registry;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BusinessValidatorRegistry {

    private final Map<String, BusinessValidator> validatorsByName;

    public BusinessValidatorRegistry(List<BusinessValidator> validators) {
        this.validatorsByName = validators.stream()
                .collect(Collectors.toUnmodifiableMap(
                        validator -> validator.getValidatorName().trim(),
                        Function.identity(),
                        (left, right) -> {
                            throw new IllegalStateException("Duplicate validator bean registered for rule "
                                    + left.getValidatorName());
                        }
                ));
    }

    public BusinessValidator getRequiredValidator(String validatorName) {
        String normalizedValidatorName = validatorName == null ? null : validatorName.trim();
        BusinessValidator validator = validatorsByName.get(normalizedValidatorName);
        if (validator == null) {
            throw new PaymentValidationException(
                    ErrorCode.INVALID_VALIDATION_RULE_CONFIGURATION,
                    "No validator bean registered for active rule " + validatorName
            );
        }

        return validator;
    }

    @PostConstruct
    void validateRegistry() {
        if (validatorsByName.isEmpty()) {
            throw new IllegalStateException("No business validators are registered");
        }
    }
}
