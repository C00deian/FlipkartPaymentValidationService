package com.flipkartclone.payments.service.interfaces;

import com.flipkartclone.payments.pojo.Payment;



public interface BusinessValidator {
    void validate(Payment request);
}
