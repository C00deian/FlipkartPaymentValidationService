package com.flipkartclone.payments.service;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentValidationImpl implements PaymentValidationService{
    @Override
    public void validatePayment(CreatePaymentRequest req) {
        log.info("Validating payment request for orderId: {}","1234");
    }
}
