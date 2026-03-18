package com.flipkartclone.payments.service;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import static com.flipkartclone.payments.constant.Constant.HMAC_SHA256;

@Service
@RequiredArgsConstructor
@Slf4j
public class HmacSha256Service {

    private String generateSignature(String jsonPayload) {

        try {

log.info("Generating HMAC signature for payload: {}", jsonPayload);

            Mac mac = Mac.getInstance(HMAC_SHA256);

            SecretKeySpec secretKey =
                    new SecretKeySpec(Constant.SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);

            mac.init(secretKey);

            byte[] rawHmac = mac.doFinal(jsonPayload.getBytes(StandardCharsets.UTF_8));

            log.info("Raw hmac :  {}", rawHmac);

            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {

            log.error("HMAC computation failed", e);

            throw new PaymentValidationException(
                    ErrorCode.HMAC_COMPUTATION_FAILED,
                    ErrorCode.HMAC_COMPUTATION_FAILED.getErrorMessage()
            );
        }
    }

    public void validateHmacSignature(String jsonPayload, String headerHmacSignature) {

        if (headerHmacSignature == null || headerHmacSignature.isBlank()) {

            log.error("Missing HMAC signature in request header");

            throw new PaymentValidationException(
                    ErrorCode.MISSING_HMAC_SIGNATURE,
                    ErrorCode.MISSING_HMAC_SIGNATURE.getErrorMessage()
            );
        }


        String calculatedHmac = generateSignature(jsonPayload);
        log.info("Calculated Hmac: {}", calculatedHmac);

        if (!MessageDigest.isEqual(
                calculatedHmac.getBytes(StandardCharsets.UTF_8),
                headerHmacSignature.getBytes(StandardCharsets.UTF_8))) {

            log.error("Invalid HMAC signature");

            throw new PaymentValidationException(
                    ErrorCode.INVALID_HMAC_SIGNATURE,
                    ErrorCode.INVALID_HMAC_SIGNATURE.getErrorMessage()
            );
        }
    }
}