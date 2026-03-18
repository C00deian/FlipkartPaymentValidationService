package com.flipkartclone.payments.controller;

import com.flipkartclone.payments.constant.Constant;
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
        // Agar control yahan aaya hai, matlab Filter ne HMAC validate kar liya hai ✅
        paymentValidationImpl.validateAndCreatePayment(req);
        return ResponseEntity.ok("Payment request valid");
    }


    @GetMapping
    public String getPaymentStatus() {
      log.info("Received request to get payment status");
      return "Get Payment Status API is under construction";
    }
}
