package com.flipkartclone.payments.controller;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.pojo.Payment;

import com.flipkartclone.payments.service.interfaces.PaymentValidationService;
import com.flipkartclone.payments.stripeprovider.SPPaymentResponse;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentValidationController {

private final PaymentValidationService paymentValidationService;

   @Value("${environment-name}")
   private String environmentName;

    @Value("${stripe.provider.createPaymentUrl}")
    private String createStripeProviderPaymentUrl;

    @PostMapping
    public SPPaymentResponse createPayment(
            @Valid @RequestBody Payment req,
            @RequestHeader(value = "X-Signature", required = false) String signature
    ) {
        log.info("Creating payment for userId: {}. Signature received: {}",
                req.getEndUserID(), (signature != null));

        SPPaymentResponse serviceResponse = paymentValidationService.validateAndCreatePayment(req);
//        log.info("order-service payload received: {}", serviceResponse);

        log.info("Payment created... checkout-url prepared for user-id: {}", req.getEndUserID());
        return serviceResponse;
    }


    @GetMapping
    public String getPaymentStatus() {
      log.info("Received request to get payment status");
      return "Get Payment Status API is under construction";
    }


    @PostConstruct
    public void init(){
        log.info("Service-name: {}, Environment: {}", Constant.SERVICE_NAME, environmentName);
        log.info("Service-name: {}, createStripeProviderPaymentUrl: {}", Constant.SERVICE_NAME, createStripeProviderPaymentUrl);
    }
}
