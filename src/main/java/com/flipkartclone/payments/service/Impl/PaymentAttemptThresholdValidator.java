package com.flipkartclone.payments.service.Impl;

import java.util.Map;

import com.flipkartclone.payments.cache.ValidatorRuleCacheRedisV3;
import com.flipkartclone.payments.constant.ValidatorRuleEnum;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.Payment;

import com.flipkartclone.payments.repository.MerchantPaymentRequestRepository;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentAttemptThresholdValidator implements BusinessValidator {

    private final MerchantPaymentRequestRepository merchantReqRepo;

    private final ValidatorRuleCacheRedisV3 validatorRuleCache;

    @Override
    public void validate(Payment paymentRequest) {

        Map<String, String> paramsMap = validatorRuleCache.getValidatorParamsForRule(
                ValidatorRuleEnum.PAYMENT_ATTEMPT_THRESHOLD_RULE.getRuleName());

        log.debug("Loaded parameters for {}: {}",
                ValidatorRuleEnum.PAYMENT_ATTEMPT_THRESHOLD_RULE.getRuleName(),
                paramsMap);

        int durationInMins = Integer.parseInt(paramsMap.get("durationInMins"));
        int maxPaymentThreshold = Integer.parseInt(paramsMap.get("maxPaymentThreshold"));

        int count = merchantReqRepo.countRequestsForUserInLastMinutes(
                paymentRequest.getEndUserID(),
                durationInMins);

        log.info("Count of payment attempts for user {} in last {} minutes: {}",
                paymentRequest.getEndUserID(),
                durationInMins,
                count);

        if(count <= maxPaymentThreshold) {
            log.info("Payment request is valid, attempt count {} is within threshold {}",
                    count, maxPaymentThreshold);

            return;
        }

        log.error("Payment request exceeds attempt threshold. "
                        + "Attempt count: {}, Threshold: {}",
                count, maxPaymentThreshold);

        throw new PaymentValidationException(
                ErrorCode.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED);
    }

}
