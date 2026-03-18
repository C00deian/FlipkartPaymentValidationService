package com.flipkartclone.payments.repository;

import com.flipkartclone.payments.entity.MerchantPaymentRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantPaymentRequestRepository extends JpaRepository<MerchantPaymentRequestEntity, Integer> {


}

