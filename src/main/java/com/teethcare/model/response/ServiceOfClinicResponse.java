package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceOfClinicResponse {
        private String id;
        private String name;
        private String description;
        private BigDecimal money;
        private int duration;
        private String imageUrl;
        private String status;

}
