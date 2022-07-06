package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AvailableVoucherResponse {
    private String voucherCode;
    private Long expiredTime;
    private BigDecimal discountValue;
}
