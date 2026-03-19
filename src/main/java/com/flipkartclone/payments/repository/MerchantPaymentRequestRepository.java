package com.flipkartclone.payments.repository;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;

import java.sql.Timestamp;

public interface MerchantPaymentRequestRepository {

    Number save(MerchantPaymentRequestEntity request);

    int countRecentRequestsSince(String endUserId, Timestamp startTime);
}
