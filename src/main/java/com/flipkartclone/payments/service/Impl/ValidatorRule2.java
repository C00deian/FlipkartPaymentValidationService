package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.BusinessValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidatorRule2 implements BusinessValidator {

    @Override
    public void validate(CreatePaymentRequest request) {
        log.info("ValidatorRule2: Validating payment request for orderId: {}", "1234");

        log.info("get firstname from request and validate");
        String firstname = request.getUser().getFirstname();
        if (firstname.contains("hello")) {
            log.error("ValidatorRule2: Invalid firstname format: {}", firstname);
            throw new PaymentValidationException(ErrorCode.VALIDATOR_RULE2_FAILED,
                    "Invalid firstname format for firstname=" + firstname);
        }
        log.info("ValidatorRule2: validation passed for firstname: {}", firstname);
    }
}
