package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VoucherResponse {
    private int id;
    private String voucherCode;
    private Long createdTime;
    private Long expiredTime;
    private Integer quantity;
    private String status;
    private BigDecimal discountValue;
    private ClinicInfoResponse clinic;
}
