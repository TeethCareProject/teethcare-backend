package com.teethcare.repository;

import com.teethcare.model.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, String> {
    Voucher findVoucherByVoucherCode(String voucherCode);
    Voucher findVoucherByVoucherCodeAndStatus(String voucherCode, String status);
}