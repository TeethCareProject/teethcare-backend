package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicLoginResponse {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private String taxCode;
    private float avgRatingScore;
    private LocationResponse location;
    private String status;
}
