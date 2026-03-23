package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.cache.ValidatorRuleCacheRedisV3;
import com.flipkartclone.payments.constant.ValidatorRuleEnum;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.http.HttpRequest;
import com.flipkartclone.payments.http.HttpServiceEngine;
import com.flipkartclone.payments.pojo.PaymentRequest;
import com.flipkartclone.payments.pojo.PaymentResponse;
import com.flipkartclone.payments.service.helper.StripeProviderHelper;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.service.interfaces.PaymentValidationService;
import com.flipkartclone.payments.stripeprovider.SPPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentValidationServiceImpl implements PaymentValidationService {

    private final ApplicationContext applicationContext;
    private final ValidatorRuleCacheRedisV3 validatorRuleCache;
    private final HttpServiceEngine httpServiceEngine;
    private final StripeProviderHelper stripeProviderHelper;

    @Override
    public PaymentResponse validateAndCreatePayment(PaymentRequest paymentRequest) {
        log.info("Starting validation and payment creation for user-id : {}", paymentRequest.getUser().getEndUserID());
        // 1. Fetch rules from Cache
        List<String> validatorRules = validatorRuleCache.getValidatorRules();
        log.info("Validator rules from cache: {}", validatorRules);

        // 2. Validate rules exist
        if (validatorRules == null || validatorRules.isEmpty()) {
            log.error("No validator rules configured, skipping validations");
            throw new PaymentValidationException(
                    ErrorCode.NO_VALIDATION_RULES_CONFIGURED);
        }

        for (String ruleName : validatorRules) {
            log.info("Applying validation rule: {}", ruleName);

            // Option 1: This method now throws PaymentValidationException if rule is unknown
            Class<? extends BusinessValidator> validatorClass =
                    ValidatorRuleEnum.getValidatorClassByRule(ruleName);

            // load the validator bean from application context
            BusinessValidator validator = applicationContext.getBean(validatorClass);

            // call the validate method of the validator
            validator.validate(paymentRequest);
        }

        log.info("All business validations passed for user-id: {}", paymentRequest.getUser().getEndUserID());


        // Code to invoke processing-service to create payment in Stripe
        // 4. Create External Request
        HttpRequest httpRequest = stripeProviderHelper.createHttpRequest(paymentRequest);
        log.info("Prepared HttpRequest for Stripe provider");

        // 5. Make the HTTP Call (Uses Circuit Breaker if configured in Engine)
        ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);

        // 6. Process Response (This throws PaymentValidationException on 4xx/5xx)
        SPPaymentResponse stripeRes = stripeProviderHelper.processResponse(httpResponse);

        // 7. Map to Final Response
        PaymentResponse finalResponse = new PaymentResponse();
        finalResponse.setHostedPageUrl(stripeRes.getCheckoutUrl());

        return finalResponse;
    }
}