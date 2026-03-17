package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import com.flipkartclone.payments.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DuplicateTxnValidator implements BusinessValidator {

    private final MerchantPaymentRequestServiceImpl repository ;
    private final JsonUtil jsonUtil;

    @Override
    public void validate(CreatePaymentRequest request) {

        log.info("Validating payment request: {}", request);

        MerchantPaymentRequestEntity entity = new MerchantPaymentRequestEntity();

        entity.setEndUserId(request.getUser().getEndUserID());
        entity.setMerchantTxnReference(request.getPayment().getMerchantTxnRef());
        entity.setTransactionRequest(jsonUtil.convertObjectToJson(request));

//      int  pkId = repository.save(entity);

       int pkId  = 100; // Mocking the save operation for demonstration purposes. Replace with actual repository call.
       log.info("pkId returned from save operation: {}", pkId);

      if(pkId == -1) {
          throw new PaymentValidationException(
                  ErrorCode.DUPLICATE_TRANSACTION,
                  ErrorCode.DUPLICATE_TRANSACTION.getErrorMessage());
      }

      log.info("valid request  no duplicate transaction found for : {}", request.getPayment());
    }
}