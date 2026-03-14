package com.flipkartclone.payments.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class Payment {

    @NotBlank(message = "CURRENCY_REQUIRED")
    @Pattern(regexp = "^[A-Z]{3}$", message = "CURRENCY_INVALID")
    private String currency;

    @NotNull(message = "AMOUNT_REQUIRED")
    @Min(value = 1, message = "AMOUNT_INVALID")
    private Integer amount;

    @NotBlank(message = "BRAND_NAME_REQUIRED")
    @Size(max = 200)
    private String brandName;

    @NotBlank(message = "LOCALE_REQUIRED")
    private String locale;

    @NotBlank(message = "COUNTRY_REQUIRED")
    @Size(min = 2, max = 2)
    private String country;

    @NotBlank(message = "MERCHANT_TXN_REF_REQUIRED")
    @Size(max = 100, message = "MERCHANT_TXN_REF_TOO_LONG")
    private String merchantTxnRef;

    @NotBlank(message = "PAYMENT_METHOD_REQUIRED")
    private String paymentMethod;

    @NotBlank(message = "PROVIDER_REQUIRED")
    private String provider;

    @NotBlank(message = "PAYMENT_TYPE_REQUIRED")
    private String paymentType;

    @NotBlank(message = "SUCCESS_URL_MISSING")
    @Size(max = 500, message = "SUCCESS_URL_TOO_LONG")
    @Pattern(regexp = "^(https?://).+", message = "SUCCESS_URL_INVALID")
    private String successUrl;

    @NotBlank(message = "CANCEL_URL_MISSING")
    @Size(max = 500, message = "CANCEL_URL_TOO_LONG")
    @Pattern(regexp = "^(https?://).+", message = "CANCEL_URL_INVALID")
    private String cancelUrl;

    @Valid
    @NotNull(message = "LINE_ITEMS_NULL")
    @NotEmpty(message = "LINE_ITEMS_EMPTY")
    @Size(max = 50, message = "LINE_ITEMS_LIMIT_EXCEEDED")
    private List<LineItem> lineItems;
}