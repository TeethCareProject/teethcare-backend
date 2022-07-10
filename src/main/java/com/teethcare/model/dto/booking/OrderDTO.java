package com.teethcare.model.dto.booking;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teethcare.model.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int id;

    private String patientName;

    private String patientPhone;

    private Timestamp patientDateOfBirth;

    private String patientGender;

    private String patientAddress;

    private String patientEmail;

    private BigDecimal totalPrice;

    private String dentistName;

    private String clinicName;

    private String clinicTaxCode;

    private String clinicLocation;

    private String clinicEmail;

    private String voucherCode;

    private BigDecimal discountValue;

    private String customerServiceName;

    private List<OrderDetailDTO> orderDetailDTOs;

    public BigDecimal getFinalPrice() {
        if (voucherCode != null && discountValue != null) {
            return totalPrice.subtract(discountValue);
        } else {
            return totalPrice;
        }
    }

    @Override
    public String toString() {
        return "Clinic ";
    }
}
