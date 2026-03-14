package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;


public interface BusinessValidator {
    void validate(CreatePaymentRequest request);
}
