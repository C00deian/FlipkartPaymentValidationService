package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.cache.ValidatorRuleCache;
import com.flipkartclone.payments.entity.ValidationRule;
import com.flipkartclone.payments.model.ValidationRuleDefinition;
import com.flipkartclone.payments.repository.ValidationRuleParamRepository;
import com.flipkartclone.payments.repository.ValidationRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationRuleConfigServiceImplTest {

    @Mock
    private ValidationRuleRepository validationRuleRepository;

    @Mock
    private ValidationRuleParamRepository validationRuleParamRepository;

    @Mock
    private ValidatorRuleCache validatorRuleCache;

    @InjectMocks
    private ValidationRuleConfigServiceImpl service;

    @Test
    void shouldLoadActiveRulesWithParamsInPriorityOrder() {
        List<ValidationRule> activeRules = List.of(
                ValidationRule.builder().validatorName("DUPLICATE_TXN_RULE").priority((short) 0).isActive(true).build(),
                ValidationRule.builder().validatorName("PAYMENT_ATTEMPT_THRESHOLD_RULE").priority((short) 10).isActive(true).build()
        );

        when(validatorRuleCache.getActiveValidationRules()).thenReturn(Optional.empty());
        when(validationRuleRepository.findActiveRulesOrderedByPriority()).thenReturn(activeRules);
        when(validationRuleParamRepository.findParamsByValidatorNames(List.of("DUPLICATE_TXN_RULE", "PAYMENT_ATTEMPT_THRESHOLD_RULE")))
                .thenReturn(Map.of(
                        "PAYMENT_ATTEMPT_THRESHOLD_RULE", Map.of(
                                "durationInMins", "2",
                                "maxPaymentThreshold", "5"
                        )
                ));

        List<ValidationRuleDefinition> rules = service.getActiveValidationRules();

        assertEquals(2, rules.size());
        assertEquals("DUPLICATE_TXN_RULE", rules.getFirst().validatorName());
        assertEquals(Map.of(), rules.getFirst().params());
        assertEquals("PAYMENT_ATTEMPT_THRESHOLD_RULE", rules.get(1).validatorName());
        assertEquals("2", rules.get(1).params().get("durationInMins"));
        assertEquals("5", rules.get(1).params().get("maxPaymentThreshold"));

        verify(validationRuleRepository).findActiveRulesOrderedByPriority();
        verify(validationRuleParamRepository).findParamsByValidatorNames(
                List.of("DUPLICATE_TXN_RULE", "PAYMENT_ATTEMPT_THRESHOLD_RULE")
        );
        verify(validatorRuleCache).putActiveValidationRules(rules);
    }

    @Test
    void shouldReturnCachedRulesWhenPresent() {
        List<ValidationRuleDefinition> cachedRules = List.of(
                new ValidationRuleDefinition("DUPLICATE_TXN_RULE", (short) 0, Map.of())
        );

        when(validatorRuleCache.getActiveValidationRules()).thenReturn(Optional.of(cachedRules));

        List<ValidationRuleDefinition> rules = service.getActiveValidationRules();

        assertEquals(cachedRules, rules);
        verify(validationRuleRepository, never()).findActiveRulesOrderedByPriority();
        verify(validationRuleParamRepository, never()).findParamsByValidatorNames(anyList());
    }
}
