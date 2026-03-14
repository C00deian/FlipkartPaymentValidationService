package com.flipkartclone.payments.controller;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.Impl.PaymentValidationImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentValidationController {

   private final PaymentValidationImpl paymentValidationImpl;

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentRequest req) {
        // If we reach here, validation PASSED ✅
        log.info("Payment validation request received");
        log.info("Validating payment request for userId : {}" , req.getUser().getEndUserID());
        log.info("Validating payment request for successUrl: {}" , req.getPayment().getSuccessUrl());

        paymentValidationImpl.validateAndCreatePayment(req);

        log.info("Payment validation successful");
        return ResponseEntity.ok("Payment request valid");
    }
}
