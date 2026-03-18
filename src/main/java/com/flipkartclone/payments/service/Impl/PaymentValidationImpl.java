package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.model.ValidationContext;
import com.flipkartclone.payments.model.ValidationRuleDefinition;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.service.interfaces.PaymentValidationService;
import com.flipkartclone.payments.service.interfaces.ValidationRuleConfigService;
import com.flipkartclone.payments.service.registry.BusinessValidatorRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentValidationImpl implements PaymentValidationService {

    private final ValidationRuleConfigService validationRuleConfigService;
    private final BusinessValidatorRegistry businessValidatorRegistry;

    @Override
    public void validateAndCreatePayment(CreatePaymentRequest req) {
        log.info("Received paymentRequest : {}", req);

        List<ValidationRuleDefinition> activeRules = validationRuleConfigService.getActiveValidationRules();
        for (ValidationRuleDefinition rule : activeRules) {
            log.info("Applying validation rule: {} with priority: {}", rule.validatorName(), rule.priority());

            BusinessValidator validator = businessValidatorRegistry.getRequiredValidator(rule.validatorName());
            ValidationContext context = new ValidationContext(rule.validatorName(), rule.priority(), rule.params());

            validator.validate(req, context);
        }

        log.info("All validation rules applied successfully for merchantTxnRef: {}", req.getPayment().getMerchantTxnRef());
    }
}
