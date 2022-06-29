package com.teethcare.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "voucher")
public class Voucher {
    @Id
    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "expired_time")
    private Timestamp expiredTime;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private String status;

    @Column(name = "discount_value")
    private BigDecimal discountValue;
}
