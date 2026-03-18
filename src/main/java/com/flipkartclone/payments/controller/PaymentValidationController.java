package com.flipkartclone.payments.controller;

import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.service.interfaces.PaymentValidationService;
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

    private final PaymentValidationService paymentValidationService;

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentRequest req) {
        paymentValidationService.validateAndCreatePayment(req);
        return ResponseEntity.ok("Payment request valid");
    }


    @GetMapping
    public String getPaymentStatus() {
        log.info("Received request to get payment status");
        return "Get Payment Status API is under construction";
    }
}
