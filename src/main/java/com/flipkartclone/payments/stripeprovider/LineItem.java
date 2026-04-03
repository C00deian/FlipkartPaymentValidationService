package com.flipkartclone.payments.stripeprovider;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LineItem {

	private String currency;

	private String productName;

	private BigDecimal unitAmount;

	private Integer quantity;
}