package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Voucher findVoucherByVoucherCode(String voucherCode);
    Voucher findVoucherByVoucherCodeAndStatus(String voucherCode, String status);
    List<Voucher> findAllByExpiredTime(Timestamp expiredTime);
    List<Voucher> findAllByClinic(Clinic clinic);
    List<Voucher> findAllByClinicIsNull();
    Voucher findVoucherById(int id);

}