package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private String imageUrl;
    private String status;
    private ClinicInfoResponse clinic;
}
