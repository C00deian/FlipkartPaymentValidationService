package com.flipkartclone.payments.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreatePaymentRequest {

    @NotBlank(message = "SUCCESS_URL_MISSING")
    @Size(max = 500, message = "SUCCESS_URL_TOO_LONG")
    @Pattern(
            regexp = "^(https?://).+",
            message = "SUCCESS_URL_INVALID"
    )
    private String successUrl;

    @NotBlank(message = "CANCEL_URL_MISSING")
    @Size(max = 500, message = "CANCEL_URL_TOO_LONG")
    @Pattern(
            regexp = "^(https?://).+",
            message = "CANCEL_URL_INVALID"
    )
    private String cancelUrl;

    @Valid
    @NotNull(message = "LINE_ITEMS_NULL")
    @NotEmpty(message = "LINE_ITEMS_EMPTY")
    @Size(max = 50, message = "LINE_ITEMS_LIMIT_EXCEEDED")
    private List<LineItem> lineItems;
}