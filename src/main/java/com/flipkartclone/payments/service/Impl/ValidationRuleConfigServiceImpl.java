package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.cache.ValidatorRuleCache;
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
    private final ValidatorRuleCache validatorRuleCache;

    @Override
    public List<ValidationRuleDefinition> getActiveValidationRules() {
        var cachedRules = validatorRuleCache.getActiveValidationRules();
        if (cachedRules.isPresent()) {
            log.info("Loaded active validation rules: ruleCount={}  cachedRules={}  ", cachedRules.get().size() , cachedRules.get());
            return cachedRules.get();
        }

        List<ValidationRule> activeRules = validationRuleRepository.findActiveRulesOrderedByPriority();

        if (activeRules.isEmpty()) {
            log.warn("No active validation rules found in validations.validation_rules");
            validatorRuleCache.evictActiveValidationRules();
            return List.of();
        }

        List<String> validatorNames = activeRules.stream()
                .map(ValidationRule::getValidatorName)
                .toList();

        Map<String, Map<String, String>> paramsByValidator =
                validationRuleParamRepository.findParamsByValidatorNames(validatorNames);

        List<ValidationRuleDefinition> resolvedRules = activeRules.stream()
                .map(rule -> new ValidationRuleDefinition(
                        rule.getValidatorName(),
                        rule.getPriority(),
                        paramsByValidator.getOrDefault(rule.getValidatorName(), Map.of())
                ))
                .toList();

        validatorRuleCache.putActiveValidationRules(resolvedRules);
        return resolvedRules;
    }
}
