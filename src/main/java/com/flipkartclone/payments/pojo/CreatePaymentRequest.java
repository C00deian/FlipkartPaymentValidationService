package com.flipkartclone.payments.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequest {

    @Valid
    @NotNull
    private User user;

    @Valid
    @NotNull
    private Payment payment;
}
