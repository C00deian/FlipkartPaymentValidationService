package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.PaymentRequest;
import com.flipkartclone.payments.pojo.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentValidationService {
      PaymentResponse validateAndCreatePayment(PaymentRequest req);
}
