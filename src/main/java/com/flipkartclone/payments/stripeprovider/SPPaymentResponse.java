package com.flipkartclone.payments.stripeprovider;

import lombok.Data;

@Data
public class SPPaymentResponse {

	private String sessionId;
	private String checkoutUrl;
}