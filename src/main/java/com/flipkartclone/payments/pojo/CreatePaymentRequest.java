package com.flipkartclone.payments.pojo;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;


import java.util.List;

@Data
public class CreatePaymentRequest {

    @NotBlank(message = "success URL is missing in the CreatePaymentRequest")
    private String successUrl;

    @NotBlank(message = "cancel URL is missing in the CreatePaymentRequest")
    private String cancelUrl;

    @Valid
    @NotEmpty(message = "lineItems cannot be empty")
    private List<LineItem> lineItems;
}