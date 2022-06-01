package com.teethcare.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClinicFilterRequest {
    String id;
    String name;
    Integer wardId;
    Integer districtId;
    Integer provinceId;
    String serviceId;
    String serviceName;
    String status;
}
