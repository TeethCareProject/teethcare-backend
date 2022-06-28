package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClinicSimpleResponse {
    private int id;
    private String name;
    private Long startTimeShift1;
    private Long endTimeShift1;
    private Long startTimeShift2;
    private Long endTimeShift2;
}
