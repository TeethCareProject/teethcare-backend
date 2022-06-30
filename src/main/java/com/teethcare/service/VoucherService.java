package com.teethcare.service;


import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.request.VoucherUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService extends CRUDService<Voucher> {
    Voucher findByVoucherCode(String voucherCode);
    Page<Voucher> findAllWithFilter(VoucherFilterRequest voucherFilterRequest, Pageable pageable);
    Voucher addNew(VoucherRequest voucherRequest);
    void deleteByVoucherCode(String voucherCode);
    void updateByVoucherCode(String voucherCode, VoucherUpdateRequest voucherUpdateRequest);
    boolean isAvailable(String voucherCode, Integer clinicId);
    void useVoucher(String voucherCode, Clinic clinic);
    void deactivate(Voucher voucher);
    List<Voucher> findAllVouchersByExpiredTime(long expiredTime);
    Voucher addByManager(VoucherRequest voucherRequest, Manager manager);
    Voucher addByAdmin(VoucherRequest voucherRequest);
}
