package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.model.ValidationRuleDefinition;

import java.util.List;

public interface ValidationRuleConfigService {
    List<ValidationRuleDefinition> getActiveValidationRules();
}
