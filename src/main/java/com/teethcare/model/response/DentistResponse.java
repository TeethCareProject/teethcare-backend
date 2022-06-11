package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DentistResponse extends AccountResponse {
    private String specialization;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClinicInfoResponse clinic;
}
