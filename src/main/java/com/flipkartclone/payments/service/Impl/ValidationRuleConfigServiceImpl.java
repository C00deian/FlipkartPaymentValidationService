package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.entity.ValidationRule;
import com.flipkartclone.payments.model.ValidationRuleDefinition;
import com.flipkartclone.payments.repository.ValidationRuleParamRepository;
import com.flipkartclone.payments.repository.ValidationRuleRepository;
import com.flipkartclone.payments.service.interfaces.ValidationRuleConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationRuleConfigServiceImpl implements ValidationRuleConfigService {

    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRuleParamRepository validationRuleParamRepository;

    @Override
    public List<ValidationRuleDefinition> getActiveValidationRules() {
        List<ValidationRule> activeRules = validationRuleRepository.findActiveRulesOrderedByPriority();

        if (activeRules.isEmpty()) {
            log.warn("No active validation rules found in validations.validation_rules");
            return List.of();
        }

        List<String> validatorNames = activeRules.stream()
                .map(ValidationRule::getValidatorName)
                .toList();

        Map<String, Map<String, String>> paramsByValidator =
                validationRuleParamRepository.findParamsByValidatorNames(validatorNames);

        return activeRules.stream()
                .map(rule -> new ValidationRuleDefinition(
                        rule.getValidatorName(),
                        rule.getPriority(),
                        paramsByValidator.getOrDefault(rule.getValidatorName(), Map.of())
                ))
                .toList();
    }
}
