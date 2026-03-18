package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.model.ValidationContext;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.service.interfaces.MerchantPaymentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentAttemptThresholdValidator implements BusinessValidator {

    private final MerchantPaymentRequestService merchantPaymentRequestService;

    @Override
    public String getValidatorName() {
        return Constant.PAYMENT_ATTEMPT_THRESHOLD;
    }

    @Override
    public void validate(CreatePaymentRequest request, ValidationContext context) {
        log.info("validating number PaymentReq for PaymentAttemptThreshold: {}", request);
        int movingWindowMin = context.getRequiredInt(Constant.DURATION_IN_MINS);
        int maxAllowedAttempts = context.getRequiredInt(Constant.MAX_PAYMENT_THRESHOLD);
        int count = merchantPaymentRequestService.countRecentRequests(request.getUser().getEndUserID(), movingWindowMin);
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
