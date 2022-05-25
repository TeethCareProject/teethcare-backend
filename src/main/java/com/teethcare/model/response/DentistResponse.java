package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DentistResponse extends AccountResponse{
    private String specialization;
    private String description;
    private String clinicId;
    private String clinicName;
}
