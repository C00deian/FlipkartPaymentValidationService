package com.flipkartclone.payments.stripeprovider;

import lombok.Data;

@Data
public class SPErrorResponse {
    private Error errorCode;
    private String errorMessage;
}
