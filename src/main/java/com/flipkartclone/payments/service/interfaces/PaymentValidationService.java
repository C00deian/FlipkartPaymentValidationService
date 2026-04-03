package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.Payment;

import com.flipkartclone.payments.pojo.PaymentResponse;
import com.flipkartclone.payments.stripeprovider.SPPaymentResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentValidationService {
      SPPaymentResponse validateAndCreatePayment(Payment req);
}
