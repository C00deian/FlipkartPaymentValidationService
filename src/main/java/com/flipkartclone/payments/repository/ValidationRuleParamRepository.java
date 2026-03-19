package com.flipkartclone.payments.repository;

import java.util.List;
import java.util.Map;

public interface ValidationRuleParamRepository {

    Map<String, Map<String, String>> findParamsByValidatorNames(List<String> validatorNames);
}
