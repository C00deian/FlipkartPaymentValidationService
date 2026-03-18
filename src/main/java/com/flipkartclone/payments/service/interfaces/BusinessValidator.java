package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.model.ValidationContext;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;


public interface BusinessValidator {
    String getValidatorName();

    void validate(CreatePaymentRequest request, ValidationContext context);
}
