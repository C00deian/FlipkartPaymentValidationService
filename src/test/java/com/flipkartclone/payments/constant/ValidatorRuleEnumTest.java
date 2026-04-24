package com.flipkartclone.payments.constant;

import com.flipkartclone.payments.service.Impl.DuplicateTxnValidator;
import com.flipkartclone.payments.service.Impl.PaymentAttemptThresholdValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorRuleEnumTest {

    @Test
    void shouldReturnValidatorClassByRuleName() {
        assertEquals(DuplicateTxnValidator.class,
                ValidatorRuleEnum.getValidatorClassByRule("DUPLICATE_TXN_RULE"));
        assertEquals(PaymentAttemptThresholdValidator.class,
                ValidatorRuleEnum.getValidatorClassByRule("PAYMENT_ATTEMPT_THRESHOLD_RULE"));
    }

    @Test
    void shouldThrowForUnknownRuleName() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidatorRuleEnum.getValidatorClassByRule("UNKNOWN_RULE"));
    }
}
