package com.flipkartclone.payments.stripeprovider;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SPCreatePaymentReq {

	private String merchantTxnRef;
	private String orderId;
	private String userId;
	private Integer amount;
	private String currency;
	private String paymentMethod;
	private String provider;
	private String successUrl;
	private String cancelUrl;
	List<LineItem> lineItems;

}