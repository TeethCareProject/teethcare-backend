package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClinicInfoResponse {
    private int id;
    private LocationResponse location;
    private String name;
    private String description;
    private String imageUrl;
    private String taxCode;
    private String email;
    private float avgRatingScore;
    private Integer bookingGap;
    private String phone;
    private String status;
    private Long startTimeShift1;
    private Long endTimeShift1;
    private Long startTimeShift2;
    private Long endTimeShift2;
}
