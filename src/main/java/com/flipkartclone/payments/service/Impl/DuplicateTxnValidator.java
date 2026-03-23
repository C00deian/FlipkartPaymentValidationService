package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.PaymentRequest;
import com.flipkartclone.payments.repository.MerchantPaymentRequestRepository;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DuplicateTxnValidator implements BusinessValidator {

    private final MerchantPaymentRequestRepository repository;

    private final JsonUtil jsonUtil;

    @Override
    public void validate(PaymentRequest paymentRequest) {

        MerchantPaymentRequestEntity entity = new MerchantPaymentRequestEntity();
        entity.setEndUserId(paymentRequest.getUser().getEndUserID());
        entity.setMerchantTxnReference(paymentRequest.getPayment().getMerchantTxnRef());
        entity.setTransactionRequest(jsonUtil.convertObjectToJson(paymentRequest));

        //int pkId = new Random().nextInt(100);
        int pkId = repository.saveMerchantPaymentRequest(entity); //TODO for testing spring security, we commented temporary.

        if(pkId == -1) {// duplicate transaction detected
            log.error("Failed to save merchant payment request, possible duplicate transaction. Payment request: {}", paymentRequest);

            throw new PaymentValidationException(
                    ErrorCode.DUPLICATE_TRANSACTION);
        }


        log.info("Payment request is valid, "
                + "no duplicate transaction detected. ");
    }

}
