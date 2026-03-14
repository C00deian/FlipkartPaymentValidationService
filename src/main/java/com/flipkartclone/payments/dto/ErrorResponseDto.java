package com.flipkartclone.payments.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDto {

    private String message;
    private String service;
    private String errorCode;
    private String path;
    private LocalDateTime timestamp;
    private String details;

}