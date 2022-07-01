package com.teethcare.service;


import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService extends CRUDService<Voucher> {
    Voucher findActiveByVoucherCode(String voucherCode);
    Page<Voucher> findAllWithFilter(VoucherFilterRequest voucherFilterRequest, Pageable pageable);
    Voucher addNew(VoucherRequest voucherRequest);
    void deleteByVoucherCode(String voucherCode);
    boolean isAvailable(String voucherCode, Integer clinicId);
    void useVoucher(String voucherCode, Clinic clinic);
    void deactivate(Voucher voucher);
    List<Voucher> findAllVouchersByExpiredTime(long expiredTime);
    Voucher addByManager(Voucher voucher, Manager manager);
    Voucher addByAdmin(Voucher voucher);
    void disable(Voucher voucher);
}
