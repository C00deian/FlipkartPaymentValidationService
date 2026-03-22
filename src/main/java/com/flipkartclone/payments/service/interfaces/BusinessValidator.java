package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.PaymentRequest;


public interface BusinessValidator {
    void validate(PaymentRequest request);
}
