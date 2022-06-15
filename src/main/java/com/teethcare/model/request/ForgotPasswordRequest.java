package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgotPasswordRequest {
    @NotBlank
    private String forgotPasswordKey;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmedPassword;
}
