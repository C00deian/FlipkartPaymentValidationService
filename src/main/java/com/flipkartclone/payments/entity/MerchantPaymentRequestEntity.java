package com.flipkartclone.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "merchant_payment_request",
        schema = "validations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_merchant_txn_reference", columnNames = "merchantTxnReference")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantPaymentRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "endUserID", length = 100)
    private String endUserId;

    @Column(name = "merchantTxnReference", nullable = false, length = 100)
    private String merchantTxnReference;

    @Lob
    @Column(name = "transactionRequest", columnDefinition = "TEXT")
    private String transactionRequest;

    /**
     * Managed by database default timestamp
     */
    @Column(name = "creationDate", insertable = false, updatable = false)
    private LocalDateTime creationDate;
}