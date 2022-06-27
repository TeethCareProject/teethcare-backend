package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceOfClinicResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private String imageUrl;
    private String status;
}
