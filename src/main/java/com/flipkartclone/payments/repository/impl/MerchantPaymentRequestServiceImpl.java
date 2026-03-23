package com.flipkartclone.payments.repository.impl;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import com.flipkartclone.payments.exception.ErrorCode;
import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.repository.MerchantPaymentRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MerchantPaymentRequestServiceImpl implements MerchantPaymentRequestRepository {

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
    private static final String COUNT_MINUTES = """
            SELECT COUNT(*)
            FROM validations.merchant_payment_request
            WHERE endUserID = :endUserId
            AND creationDate >= :startTime
            """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int saveMerchantPaymentRequest(MerchantPaymentRequestEntity request) {

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
                    new String[]{"id"}
            );

            Number generatedId = keyHolder.getKey();

            if (generatedId != null) {
                int id = generatedId.intValue();

                log.info("Inserted merchant_payment_request with id: {} for merchantTxnReference: {}",
                        id,
                        request.getMerchantTxnReference());

                return id;
            }

            throw new PaymentValidationException(
                    ErrorCode.DATABASE_SAVE_FAILED
            );

        } catch (DuplicateKeyException ex) {

            log.error("Duplicate merchantTxnReference detected: {}",
                    request.getMerchantTxnReference());

            // ✅ FIX: throw proper business exception
            throw new PaymentValidationException(
                    ErrorCode.DUPLICATE_TRANSACTION,
                    "Duplicate merchantTxnReference: " + request.getMerchantTxnReference()
            );
        }
    }

    @Override
    public int countRequestsForUserInLastMinutes(String endUserId, int minutes) {

        Instant startTime = Instant.now().minus(minutes, ChronoUnit.MINUTES);

        Map<String, Object> params = Map.of(
                "endUserId", endUserId,
                "startTime", Timestamp.from(startTime)
        );

        Integer count = jdbcTemplate.queryForObject(COUNT_MINUTES, params, Integer.class);
        return count == null ? 0 : count;
    }
}
