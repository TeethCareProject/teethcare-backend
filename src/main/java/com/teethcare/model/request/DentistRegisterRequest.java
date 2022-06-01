package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DentistRegisterRequest extends PatientRegisterRequest {

    private String specialization;

    private String description;

}
