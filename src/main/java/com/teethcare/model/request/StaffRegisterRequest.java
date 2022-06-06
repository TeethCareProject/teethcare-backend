package com.teethcare.model.request;

import com.teethcare.utils.Trimmable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffRegisterRequest extends PatientRegisterRequest implements Trimmable {

    private String specialization;

    private String description;

    private String role;

}
