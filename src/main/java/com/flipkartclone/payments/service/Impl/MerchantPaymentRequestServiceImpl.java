package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.repository.MerchantPaymentRequestRepository;
import com.flipkartclone.payments.service.interfaces.MerchantPaymentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantPaymentRequestServiceImpl implements MerchantPaymentRequestService {

    private final MerchantPaymentRequestRepository merchantPaymentRequestRepository;

    @Override
    public int save(MerchantPaymentRequestEntity request) {
        try {
            Number generatedId = merchantPaymentRequestRepository.save(request);

            if (generatedId != null) {
                int id = generatedId.intValue();

                log.info("Inserted merchant_payment_request with id: {} for merchantTxnReference: {}",
                        Optional.of(id),
                        request.getMerchantTxnReference());

                return id;
            }

            log.info("Insertion completed but failed to retrieve generated id for merchantTxnReference: {}",
                    request.getMerchantTxnReference());

            throw new PaymentValidationException(ErrorCode.FAILED_TO_SAVE_PAYMENT_REQUEST ,
                    ErrorCode.FAILED_TO_SAVE_PAYMENT_REQUEST.getErrorMessage());

        } catch (DuplicateKeyException ex) {

            log.error("Duplicate merchantTxnReference detected: {}",
                    request.getMerchantTxnReference());

            return -1;
        }
    }

    @Override
    public int countRecentRequests(String endUserId, int minutes) {
        Instant startInstant = Instant.now().minus(minutes, ChronoUnit.MINUTES);
        Timestamp startTime = Timestamp.from(startInstant);
        return merchantPaymentRequestRepository.countRecentRequestsSince(endUserId, startTime);
    }
}
