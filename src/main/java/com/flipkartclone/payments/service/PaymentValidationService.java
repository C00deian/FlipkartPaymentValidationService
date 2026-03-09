package com.flipkartclone.payments.service;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import org.springframework.stereotype.Service;

@Service
public interface PaymentValidationService {
     public void validatePayment(CreatePaymentRequest req);
}
