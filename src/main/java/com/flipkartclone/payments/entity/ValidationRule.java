package com.flipkartclone.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Maps to validations.validation_rules
 */
@Entity
@Table(name = "validation_rules", schema = "validations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "validatorName", nullable = false, unique = true, length = 50)
    private String validatorName;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Column(name = "priority", nullable = false)
    private Short priority;

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


