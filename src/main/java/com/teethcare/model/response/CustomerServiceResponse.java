package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerServiceResponse extends AccountResponse {
    private String clinicId;
    private String clinicName;
}