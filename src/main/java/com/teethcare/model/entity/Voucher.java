package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "voucher")
public class Voucher {
    @Id
    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "created_time")
    private Long createdTime;

    @Column(name = "expired_time")
    private Long expiredTime;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private String status;

    @Column(name = "discount_value")
    private BigDecimal discountValue;
}
