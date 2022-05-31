package com.teethcare.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClinicFilterRequest {
    String name;
    Integer wardId;
    Integer districtId;
    Integer provinceId;
    List<Integer> serviceList;
    String status;
}
