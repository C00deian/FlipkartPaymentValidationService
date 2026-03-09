package com.flipkartclone.payments.pojo;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class LineItem {

    @NotBlank(message = "lineItem name is required")
    private String productName;

    @NotNull(message = "lineItem quantity is required")
    @Min(value = 1, message = "lineItem quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "lineItem price is required")
    @DecimalMin(value = "0.01", message = "lineItem price must be greater than 0")
    private BigDecimal unitAmount;

    @NotBlank(message = "lineItem currency is required")
    @Size(min = 3, max = 3, message = "currency must be a 3-letter ISO code e.g. INR, USD")
    private String currency;
}

