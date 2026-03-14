package com.flipkartclone.payments.constant;

import com.flipkartclone.payments.service.Impl.ValidatorRule1;
import com.flipkartclone.payments.service.Impl.ValidatorRule2;
import com.flipkartclone.payments.service.Impl.ValidatorRule3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorRuleEnumTest {

    @Test
    void shouldReturnValidatorClassByRuleName() {
        assertEquals(ValidatorRule1.class, ValidatorRuleEnum.getValidatorClassByRule("VALIDATE_RULE1"));
        assertEquals(ValidatorRule2.class, ValidatorRuleEnum.getValidatorClassByRule(" VALIDATE_RULE2 "));
        assertEquals(ValidatorRule3.class, ValidatorRuleEnum.getValidatorClassByRule("validate_rule3"));
    }

    @Test
    void shouldThrowForUnknownRuleName() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidatorRuleEnum.getValidatorClassByRule("UNKNOWN_RULE"));
    }
}

