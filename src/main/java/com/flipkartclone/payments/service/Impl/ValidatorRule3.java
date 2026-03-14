package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidatorRule3 implements BusinessValidator {

    @Override
    public void validate(CreatePaymentRequest request) {
        log.info("ValidatorRule3: Validating payment request for orderId: {}", "1234");

        String lastName = request.getUser().getLastname();
        if (lastName.contains("world")) {
            log.error("ValidatorRule3: Invalid lastName format: {}", lastName);
            throw new RuntimeException("Invalid lastName format");
        }
        log.info("ValidatorRule3: validation passed for lastName: {}", lastName);
    }

}

