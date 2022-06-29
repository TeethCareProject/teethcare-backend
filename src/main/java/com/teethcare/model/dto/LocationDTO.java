package com.teethcare.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {
    private Integer id;
    private String addressString;
    private String fullAddress;
    private Double longitude;
    private Double latitude;
    private Integer wardId;
}
