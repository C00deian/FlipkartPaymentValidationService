package com.flipkartclone.payments.util;

import org.springframework.stereotype.Service;

@Service
public class TruncateStripeMessage {

    public static String cleanStripeMessage(String message) {

        if(message == null) return "Invalid payment request";
        int index = message.indexOf(".");

        if(index != -1){
            return message.substring(0,index);
        }
        return message;
    }
}
