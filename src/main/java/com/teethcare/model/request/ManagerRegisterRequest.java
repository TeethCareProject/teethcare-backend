package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerRegisterRequest extends PatientRegisterRequest{

    @NotBlank
    @Length(max = 100)
    private String clinicName;

    @NotBlank
    @Length(max = 13)
    private String clinicTaxCode;

    @Email
    @NotBlank
    @Length(max = 320)
    private String clinicEmail;

    @NotBlank
    @Length(max = 150)
    private String clinicAddress;

    @NotNull
    private Integer wardId;
}
