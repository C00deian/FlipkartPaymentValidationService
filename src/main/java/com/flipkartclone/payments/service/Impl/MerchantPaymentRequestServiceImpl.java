package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.repository.MerchantPaymentRequestRepository;
import com.flipkartclone.payments.service.interfaces.MerchantPaymentRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantPaymentRequestServiceImpl implements MerchantPaymentRequestService {

    private final MerchantPaymentRequestRepository merchantPaymentRequestRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
            INSERT INTO validations.merchant_payment_request
            (
                merchantTxnReference,
                endUserID,
                transactionRequest
            )
            VALUES
            (
                :merchantTxnReference,
                :endUserID,
                :transactionRequest
            )
            """;

    @Override
    public int save(MerchantPaymentRequestEntity request) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("merchantTxnReference", request.getMerchantTxnReference())
                .addValue("endUserID", request.getEndUserId())
                .addValue("transactionRequest", request.getTransactionRequest());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {

            jdbcTemplate.update(
                    INSERT_SQL,
                    params,
                    keyHolder,
                    new String[]{"id"} // auto-generated column
            );

            Number generatedId = keyHolder.getKey();

            if (generatedId != null) {
                int id = generatedId.intValue();

                log.info("Inserted merchant_payment_request with id: {} for merchantTxnReference: {}",
                        id,
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
}