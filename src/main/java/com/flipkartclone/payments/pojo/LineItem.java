package com.flipkartclone.payments.pojo;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class LineItem {

    @NotBlank(message = "PRODUCT_NAME_MISSING")
    @Size(max = 200, message = "PRODUCT_NAME_TOO_LONG")
    private String productName;

    @NotNull(message = "QUANTITY_REQUIRED")
    @Min(value = 1, message = "QUANTITY_INVALID")
    private Integer quantity;

    @NotNull(message = "UNIT_AMOUNT_REQUIRED")
    @DecimalMin(value = "0.01", message = "UNIT_AMOUNT_INVALID")
    private BigDecimal unitAmount;

    @NotBlank(message = "CURRENCY_REQUIRED")
    @Pattern(regexp = "^[A-Z]{3}$", message = "CURRENCY_INVALID")
    private String currency;
}

