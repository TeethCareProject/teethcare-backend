package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank
    @NotNull
    private String oldPassword;
    @NotBlank
    @NotNull
    private String newPassword;
    @NotBlank
    @NotNull
    private String confirmPassword;
}
