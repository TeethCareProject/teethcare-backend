package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerRegisterRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private String gender;
    @NotEmpty
    private String clinicName;
    @NotEmpty
    private String clinicTaxCode;
    @NotEmpty
    private String clinicAddress;
    @NotEmpty
    private int provinceId;
    @NotEmpty
    private int districtId;
    @NotEmpty
    private int wardId;
}
