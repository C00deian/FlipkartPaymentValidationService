package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.cache.ValidatorRuleCacheRedisV3;
import com.flipkartclone.payments.constant.ValidatorRuleEnum;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.service.interfaces.PaymentValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentValidationImpl implements PaymentValidationService {

    private  final ApplicationContext applicationContext;
    private final ValidatorRuleCacheRedisV3 validatorRuleCache;



    @Override
    public void validateAndCreatePayment(CreatePaymentRequest req) {

        log.info("Received paymentRequest : {}", req);

        List<String> validatorRules = validatorRuleCache.getValidatorRules();
        log.info("validator rule form cacheRedis {}" , validatorRules );



        if (validatorRules == null || validatorRules.isEmpty()) {
            throw new PaymentValidationException(
                    ErrorCode.NO_VALIDATION_RULE_CONFIGURED,
                    ErrorCode.NO_VALIDATION_RULE_CONFIGURED.getErrorMessage()
            );
        }

        for (String rule : validatorRules) {
            log.info("Applying validation rule : {}", rule);

            try {
                Class<? extends BusinessValidator> validatorClass =
                        ValidatorRuleEnum.getValidatorClassByRule(rule.trim());

                if (validatorClass == null) {
                    log.warn("No validator found for rule: {}", rule);
                    continue;
                }

                BusinessValidator validator = applicationContext.getBean(validatorClass);

                validator.validate(req);

                log.info("Validation passed for rule: {}", rule);

            } catch (PaymentValidationException ex) {
                log.error("Business validation failed for rule: {} message: {}", rule, ex.getMessage());
                throw ex;

            } catch (Exception ex) {
                log.error("Unexpected error for rule: {}", rule, ex);

                throw new PaymentValidationException(
                        ErrorCode.GENERIC_ERROR_CODE,
                        ErrorCode.GENERIC_ERROR_CODE.getErrorMessage());

            }
        }
    }

}
