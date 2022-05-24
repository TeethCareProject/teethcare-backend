package com.teethcare.config.model.response;

import com.teethcare.config.model.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClinicInfoResponse {
    private Integer id;
    private Location location;
    private String name;
    private String description;
    private String imageUrl;
    private String taxCode;
    private Float avgRatingScore;
    private String status;
}
