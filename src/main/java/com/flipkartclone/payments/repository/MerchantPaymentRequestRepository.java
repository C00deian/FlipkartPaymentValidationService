package com.flipkartclone.payments.repository;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
@RequiredArgsConstructor
public class MerchantPaymentRequestRepository {

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

    private static final String COUNT_RECENT_REQUESTS_SQL = """
            SELECT COUNT(*)
            FROM validations.merchant_payment_request
            WHERE endUserID = :endUserId
            AND creationDate >= :startTime
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Number save(MerchantPaymentRequestEntity request) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("merchantTxnReference", request.getMerchantTxnReference())
                .addValue("endUserID", request.getEndUserId())
                .addValue("transactionRequest", request.getTransactionRequest());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL, params, keyHolder, new String[]{"id"});
        return keyHolder.getKey();
    }

    public int countRecentRequestsSince(String endUserId, Timestamp startTime) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("endUserId", endUserId)
                .addValue("startTime", startTime);

        Integer count = jdbcTemplate.queryForObject(COUNT_RECENT_REQUESTS_SQL, params, Integer.class);
        return count == null ? 0 : count;
    }
}
