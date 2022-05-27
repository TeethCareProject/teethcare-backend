package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerRegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String gender;

    @NotEmpty
    @Pattern(regexp = "(0)+(\\d){9}")
    private String phoneNumber;

    @NotBlank
    private String clinicName;

    @NotBlank
    private String clinicTaxCode;

    @NotBlank
    private String clinicAddress;

    private int wardId;
}
