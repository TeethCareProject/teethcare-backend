package com.teethcare.model.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private BigDecimal discountValue = BigDecimal.ZERO;

    private String customerServiceName;
    private Timestamp examinationTime;

    private List<OrderDetailDTO> orderDetailDTOs;
    private String finalPrice;

    public BigDecimal getFinalPrice() {
        if (voucherCode != null) {
            BigDecimal finalPrice = BigDecimal.valueOf(0);
            if (discountValue.compareTo(totalPrice) < 0) {
                return totalPrice.subtract(discountValue);
            }
            return finalPrice;
        } else {
            this.discountValue = BigDecimal.valueOf(0);
            return totalPrice;
        }
    }
}
