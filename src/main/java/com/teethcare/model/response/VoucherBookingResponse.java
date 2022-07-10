package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VoucherBookingResponse {
    private int id;
    private String voucherCode;
    private Long createdTime;
    private Long expiredTime;
    private Integer quantity;
    private BigDecimal discountValue;
}