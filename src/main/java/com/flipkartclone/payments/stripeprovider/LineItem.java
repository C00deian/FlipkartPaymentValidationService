package com.flipkartclone.payments.stripeprovider;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineItem {

	private String currency;

	private String productName;

	private BigDecimal unitAmount;

	private Integer quantity;
}