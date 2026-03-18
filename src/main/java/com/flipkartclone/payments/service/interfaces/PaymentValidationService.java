package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;

public interface PaymentValidationService {
    void validateAndCreatePayment(CreatePaymentRequest req);
}
