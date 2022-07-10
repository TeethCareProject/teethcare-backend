package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VoucherUpdateRequest {
    private Long expiredTime;
    private Integer quantity;
    private BigDecimal discountValue;
}
