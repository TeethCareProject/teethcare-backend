package com.teethcare.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClinicFilterRequest {
    String name;
    Integer wardId;
    Integer districtId;
    Integer provinceId;
}
