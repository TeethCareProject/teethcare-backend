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

    private String clinicPhone;

    private String voucherCode;

    private BigDecimal discountValue = BigDecimal.valueOf(0);

    private String customerServiceName;
    private Timestamp examinationTime;

    private List<OrderDetailDTO> orderDetailDTOs;

    public BigDecimal getFinalPrice() {
        if (voucherCode != null && discountValue != null) {
            BigDecimal finalPrice = BigDecimal.valueOf(0);
            if (discountValue.compareTo(finalPrice) < 0) {
                return totalPrice.subtract(discountValue);
            }
            return finalPrice;
        } else {
            return totalPrice;
        }
    }
}
