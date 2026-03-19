package com.flipkartclone.payments.cache;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.model.ValidationRuleDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ValidatorRuleCache {

    private final ObjectProvider<RedisTemplate<String, String>> redisTemplateProvider;

    public ValidatorRuleCache(ObjectProvider<RedisTemplate<String, String>> redisTemplateProvider) {
        this.redisTemplateProvider = redisTemplateProvider;
    }

    public Optional<List<ValidationRuleDefinition>> getActiveValidationRules() {
        RedisTemplate<String, String> redisTemplate = redisTemplateProvider.getIfAvailable();
        if (redisTemplate == null) {
            log.info("RedisTemplate not available. validation-rule cache is bypassed.");
            return Optional.empty();
        }

        ListOperations<String, String> listOps = redisTemplate.opsForList();
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

        List<String> validatorNames = listOps.range(Constant.VALIDATION_RULES_ACTIVE_LIST_KEY, 0, -1);
        if (validatorNames == null || validatorNames.isEmpty()) {
            log.info("Validation-rule cache miss. key={}", Constant.VALIDATION_RULES_ACTIVE_LIST_KEY);
            return Optional.empty();
        }

        List<ValidationRuleDefinition> cachedRules = validatorNames.stream().map(validatorName -> {
            String priorityValue = hashOps.get(Constant.VALIDATION_RULES_PRIORITY_HASH_KEY, validatorName);
            short priority = parsePriority(priorityValue);
            Map<String, String> params = hashOps.entries(getParamsHashKey(validatorName));

            return new ValidationRuleDefinition(
                    validatorName,
                    priority,
                    params
            );
        }).toList();

        log.info("Validation-rule cache hit. ruleCount={} names={}", cachedRules.size(), validatorNames);
        return Optional.of(cachedRules);
    }

    public void putActiveValidationRules(List<ValidationRuleDefinition> rules) {
        if (rules == null || rules.isEmpty()) {
            return;
        }

        RedisTemplate<String, String> redisTemplate = redisTemplateProvider.getIfAvailable();
        if (redisTemplate == null) {
            log.info("Skipping cache put. RedisTemplate not available.");
            return;
        }

        ListOperations<String, String> listOps = redisTemplate.opsForList();
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

        evictActiveValidationRules();

        rules.stream()
                .map(ValidationRuleDefinition::validatorName)
                .forEach(name -> listOps.rightPush(Constant.VALIDATION_RULES_ACTIVE_LIST_KEY, name));

        for (ValidationRuleDefinition rule : rules) {
            hashOps.put(
                    Constant.VALIDATION_RULES_PRIORITY_HASH_KEY,
                    rule.validatorName(),
                    Short.toString(rule.priority())
            );

            if (!rule.params().isEmpty()) {
                hashOps.putAll(getParamsHashKey(rule.validatorName()), rule.params());
            }
        }

        log.info("Validation-rule cache populated. ruleCount={} listKey={}",
                rules.size(),
                Constant.VALIDATION_RULES_ACTIVE_LIST_KEY);
    }

    public void evictActiveValidationRules() {
        RedisTemplate<String, String> redisTemplate = redisTemplateProvider.getIfAvailable();
        if (redisTemplate == null) {
            return;
        }

        ListOperations<String, String> listOps = redisTemplate.opsForList();
        List<String> validatorNames = listOps.range(Constant.VALIDATION_RULES_ACTIVE_LIST_KEY, 0, -1);
        if (validatorNames != null) {
            validatorNames.forEach(name -> redisTemplate.delete(getParamsHashKey(name)));
        }

        redisTemplate.delete(Constant.VALIDATION_RULES_ACTIVE_LIST_KEY);
        redisTemplate.delete(Constant.VALIDATION_RULES_PRIORITY_HASH_KEY);
        log.info("Validation-rule cache evicted.");
    }

    private short parsePriority(String value) {
        try {
            return value == null ? 0 : Short.parseShort(value);
        } catch (NumberFormatException ex) {
            log.warn("Invalid cached priority value: {}", value);
            return 0;
        }
    }

    private String getParamsHashKey(String validatorName) {
        return Constant.VALIDATION_RULES_PARAMS_HASH_KEY_PREFIX + validatorName;
    }


}
