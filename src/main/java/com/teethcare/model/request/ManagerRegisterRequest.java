package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String clinicName;
    @NotBlank
    private String clinicTaxCode;
    @NotBlank
    private String clinicAddress;
    private int provinceId;
    private int districtId;
    private int wardId;
}
