package com.flipkartclone.payments.constant;

public class Constant {
    private Constant() {
    }


    public static final String SECRET_KEY = "THIS_IS_MY_SECRET";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static  final String MERCHANT_ID = "merchant101";
    public static final String ROLE_MERCHANT = "ROLE_MERCHANT";
    public static final String HMAC_SIGNATURE = "X-Signature";
    public static final String DUPLICATE_TXN = "DUPLICATE_TXN_RULE";
    public static final String PAYMENT_ATTEMPT_THRESHOLD = "PAYMENT_ATTEMPT_THRESHOLD_RULE";
    public static final String DURATION_IN_MINS = "durationInMins";
    public static final String MAX_PAYMENT_THRESHOLD = "maxPaymentThreshold";

    // Service Name
    public static final String SERVICE_NAME = "Stripe Provider Service";

//    public static final String CREATE_SESSION_MODE = "mode";
//    public static final String PAYMENT_SUCCESS_URL = "success_url";
//    public static final String PAYMENT_CANCEL_URL = "cancel_url";
//
//    // Payment Mode Constants
//    public static final String PAYMENT_MODE_VALUE = "payment";
//
//    // Line Items Form Data Constants
//    public static final String LINE_ITEMS_PRICE_DATA_CURRENCY = "line_items[%d][price_data][currency]";
//    public static final String LINE_ITEMS_PRICE_DATA_UNIT_AMOUNT = "line_items[%d][price_data][unit_amount]";
//    public static final String LINE_ITEMS_PRICE_DATA_PRODUCT_NAME = "line_items[%d][price_data][product_data][name]";
//    public static final String LINE_ITEMS_QUANTITY = "line_items[%d][quantity]";
//
//    // Unit Amount Multiplier
//    public static final int UNIT_AMOUNT_MULTIPLIER = 100;
}

