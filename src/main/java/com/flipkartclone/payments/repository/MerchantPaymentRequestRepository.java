package com.flipkartclone.payments.repository;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;


public interface MerchantPaymentRequestRepository {

     int saveMerchantPaymentRequest(MerchantPaymentRequestEntity
                                                  entity);
     int countRequestsForUserInLastMinutes(
            String endUserId, int minutes);

}
