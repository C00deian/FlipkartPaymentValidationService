package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentAttemptThresholdValidator implements BusinessValidator {

    private final MerchantPaymentRequestServiceImpl repository;

    @Override
    public void validate(CreatePaymentRequest request) {
        log.info("validating number PaymentReq for PaymentAttemptThreshold: {}", request);
        int movingWindowMin = 10;
        int maxAllowedAttempts = 5;
        int count = repository.countRecentRequests(request.getUser().getEndUserID(), movingWindowMin);
        log.info("count of Payments attempts in last {} minutes for user {} is : {}", movingWindowMin, request.getUser().getEndUserID(), count);

        if (count <= maxAllowedAttempts) {
            log.info("Payment attempt count {} is within the threshold for user {}", count, request.getUser().getEndUserID());
        } else {
            log.info("Payment attempt count {} exceeds the threshold for user {}", count, request.getUser().getEndUserID());
            throw new PaymentValidationException(
                    ErrorCode.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED,
                    ErrorCode.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED.getErrorMessage()
            );
        }

    }
}
