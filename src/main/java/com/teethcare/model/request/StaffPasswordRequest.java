package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class StaffPasswordRequest {
    @NotBlank
    @Length(max = 72)
    private String password;

    @NotBlank
    @Length(max = 72)
    private String confirmPassword;
}