package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import org.springframework.stereotype.Service;

@Service
public interface PaymentValidationService {
      String validateAndCreatePayment(CreatePaymentRequest req);
}
