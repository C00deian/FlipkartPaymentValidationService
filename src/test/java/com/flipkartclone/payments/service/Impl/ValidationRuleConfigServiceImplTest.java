package com.flipkartclone.payments.service.Impl;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationRuleConfigServiceImplTest {

    @Mock
    private ValidationRuleRepository validationRuleRepository;

    @Mock
    private ValidationRuleParamRepository validationRuleParamRepository;

    @InjectMocks
    private ValidationRuleConfigServiceImpl service;

    @Test
    void shouldLoadActiveRulesWithParamsInPriorityOrder() {
        List<ValidationRule> activeRules = List.of(
                ValidationRule.builder().validatorName("DUPLICATE_TXN_RULE").priority((short) 0).isActive(true).build(),
                ValidationRule.builder().validatorName("PAYMENT_ATTEMPT_THRESHOLD_RULE").priority((short) 10).isActive(true).build()
        );

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
    }
}
