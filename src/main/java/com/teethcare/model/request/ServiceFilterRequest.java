package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFilterRequest {
    private String name;
    private Integer clinicID;
    private BigDecimal lowerPrice;
    private BigDecimal upperPrice;
}
