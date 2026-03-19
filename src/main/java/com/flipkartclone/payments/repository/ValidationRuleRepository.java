package com.flipkartclone.payments.repository;

import com.flipkartclone.payments.entity.ValidationRule;

import java.util.List;

public interface ValidationRuleRepository {

    List<ValidationRule> findActiveRulesOrderedByPriority();
}
