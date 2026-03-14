package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidatorRule1 implements BusinessValidator {


    @Override
    public void validate(CreatePaymentRequest request) {
        log.info("ValidatorRule1: Validating payment request for orderId: {}", "1234");
        String email = request.getUser().getEmail();
        if (!email.contains("@")) {
            log.error("ValidatorRule1: Invalid email format: {}", email);
            throw new RuntimeException("Invalid email format");
        }
        log.info("ValidatorRule1: Email validation passed for email: {}", email);
    }


}

