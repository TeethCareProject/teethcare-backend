package com.teethcare.model.request;

import com.teethcare.utils.Trimmable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerRegisterRequest extends PatientRegisterRequest implements Trimmable {

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

    private Double longitude;

    private Double latitude;

    @NotNull
    private Long startTimeShift1;

    @NotNull
    private Long endTimeShift1;

    @NotNull
    private Long startTimeShift2;

    @NotNull
    private Long endTimeShift2;
}
