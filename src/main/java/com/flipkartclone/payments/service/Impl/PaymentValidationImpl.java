package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.constant.ValidatorRuleEnum;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.HmacSha256Service;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.service.interfaces.PaymentValidationService;
import com.flipkartclone.payments.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentValidationImpl implements PaymentValidationService {

    private final ApplicationContext applicationContext;
    private final HmacSha256Service hmacSha256Service;
    private final JsonUtil jsonUtil;

    @Value("${validate-rule-names}")
    String validateRuleNames;

    @Override
    public String validateAndCreatePayment(CreatePaymentRequest req , String headerHmacSignature) {

        log.info("Received paymentRequest : {} and  HmacSignature: {}", req, headerHmacSignature);

        String jsonPayload = jsonUtil.convertObjectToJson(req);

       hmacSha256Service.validateHmacSignature(jsonPayload, headerHmacSignature);
        log.info("HMAC validation Passed.");

        String[] ruleNames = validateRuleNames.split(",");
        for (String ruleName : ruleNames) {
            String normalizedRuleName = ruleName.trim();
            log.info("Applying validation rule: {}", normalizedRuleName);

            Class<? extends BusinessValidator> validatorClass = ValidatorRuleEnum.getValidatorClassByRule(normalizedRuleName);

            BusinessValidator validator = applicationContext.getBean(validatorClass);

            validator.validate(req);
        }

        log.info("All validation rules applied successfully for orderId: {}", "1234");

        return "Payment validation successful for orderId: " + "1234";
    }

    @PostConstruct
    public void init() {
        log.info("Initializing PaymentValidationImpl with validateRuleNames: {}", validateRuleNames);
    }

}
