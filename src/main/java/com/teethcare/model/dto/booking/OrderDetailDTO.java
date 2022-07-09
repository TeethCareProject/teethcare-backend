package com.teethcare.model.dto.booking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teethcare.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private int id;

    private int serviceId;

    private String serviceName;

    private BigDecimal servicePrice;
}
