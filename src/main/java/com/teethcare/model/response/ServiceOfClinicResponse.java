package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOfClinicResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal money;
    private int duration;
    private String imageUrl;
    private String status;

}
