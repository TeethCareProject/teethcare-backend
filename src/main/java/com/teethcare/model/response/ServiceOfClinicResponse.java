package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teethcare.model.entity.Clinic;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceOfClinicResponse {
//    private String id;
//    private String name;
//    private String description;
//    private BigDecimal money;
//    private int duration;
//    private String imageUrl;
//    private String status;

    private String id;
    private String name;
    private String description;
    private BigDecimal money;
    private Integer duration;
    private String imageUrl;
    private String status;
    private Clinic clinic;

}
