package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class VoucherRequest {
    @NotNull
    @NotBlank
    private String voucherCode;
    private Long expiredTime;
    private Integer quantity;
    @NotNull
    private BigDecimal discountValue;
}
