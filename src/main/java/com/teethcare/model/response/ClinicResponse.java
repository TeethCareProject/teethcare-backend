package com.teethcare.model.response;

import com.teethcare.model.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClinicResponse {

    private int id;
    private AccountResponse manager;
    private Location location;
    private String name;
    private String description;
    private String imageUrl;
    private String taxCode;
    private float avgRatingScore;
    private String status;

}