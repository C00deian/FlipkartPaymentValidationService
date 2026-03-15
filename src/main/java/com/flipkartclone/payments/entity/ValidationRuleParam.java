package com.flipkartclone.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Maps to validations.validation_rules_params
 * FK: validatorName → validation_rules.validatorName
 */
@Entity
@Table(name = "validation_rules_params", schema = "validations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationRuleParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * FK reference stored as a plain string column (mirrors the DB FK on validatorName).
     * The relationship is navigated from .
     */
    @Column(name = "validatorName", nullable = false, length = 50)
    private String validatorName;

    @Column(name = "paramName", nullable = false, length = 200)
    private String paramName;

    @Column(name = "paramValue", nullable = false, length = 200)
    private String paramValue;

    @Column(name = "creationDate", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP(2) DEFAULT CURRENT_TIMESTAMP(2)")
    private LocalDateTime creationDate;

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }
}

