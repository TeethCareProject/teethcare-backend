package com.teethcare.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClinicFilterRequest {
    String searchKey;
    Integer wardId;
    Integer districtId;
    Integer provinceId;
}
