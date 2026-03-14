package com.flipkartclone.payments.pojo;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class User {

    @NotBlank(message = "USER_ID_REQUIRED")
    @Size(max = 100)
    private String endUserID;

    @NotBlank(message = "FIRST_NAME_REQUIRED")
    @Size(max = 100)
    private String firstname;

    @NotBlank(message = "LAST_NAME_REQUIRED")
    @Size(max = 100)
    private String lastname;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    private String email;

    @NotBlank(message = "MOBILE_PHONE_REQUIRED")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "MOBILE_PHONE_INVALID"
    )
    private String mobilePhone;
}