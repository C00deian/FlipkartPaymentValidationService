package com.flipkartclone.payments.service;

import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;

import com.flipkartclone.payments.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.flipkartclone.payments.constant.Constant.HMAC_SHA256;
import static com.flipkartclone.payments.constant.Constant.SECRET_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class HmacSha256Service {

    private final JsonUtil jsonUtil;

    private String generateSignature(String  jsonPayload) {
        try {
            // Create MAC instance
            Mac mac = Mac.getInstance(HMAC_SHA256);

            // Create secret key
            SecretKeySpec secretKey =
                    new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);

            // Initialize MAC with key
            mac.init(secretKey);

            // Generate HMAC
            byte[] rawHmac = mac.doFinal(jsonPayload.getBytes(StandardCharsets.UTF_8));
            log.info("Generated raw HMAC bytes: {}", Base64.getEncoder().encodeToString(rawHmac));

            // Convert to Base64 (common format used in APIs)
            String base64 = Base64.getEncoder().encodeToString(rawHmac);
            log.info("Generated Base64 HMAC: {}", base64);

            return base64;

        } catch (Exception e) {
            throw new PaymentValidationException(ErrorCode.HMAC_COMPUTATION_FAILED,
                    ErrorCode.HMAC_COMPUTATION_FAILED.getErrorMessage());
        }
    }


    public String isHmacSignatureValid(CreatePaymentRequest req, String headerHmacSignature) {
        if(headerHmacSignature == null || headerHmacSignature.isEmpty()) {
            log.error("Missing HMAC signature in request header");
            throw new PaymentValidationException(ErrorCode.MISSING_HMAC_SIGNATURE,
                    ErrorCode.MISSING_HMAC_SIGNATURE.getErrorMessage());
        }

        String jsonPayload  = jsonUtil.convertObjectToJson(req);
        String calculatedHmac  = generateSignature(jsonPayload);

        if(!calculatedHmac.equals(headerHmacSignature)) {
            log.error("HMAC calculatedHmac validation failed. Expected: {} , Received: {}", calculatedHmac, headerHmacSignature);
            throw new PaymentValidationException(ErrorCode.INVALID_HMAC_SIGNATURE,
                    ErrorCode.INVALID_HMAC_SIGNATURE.getErrorMessage());
        }
        return calculatedHmac;
    }

}