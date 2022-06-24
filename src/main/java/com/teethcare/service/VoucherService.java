package com.teethcare.service;


import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.request.VoucherUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherService extends CRUDService<Voucher> {
    Voucher findByVoucherCode(String voucherCode);
    Page<Voucher> findAllWithFilter(VoucherFilterRequest voucherFilterRequest, Pageable pageable);
    Voucher addNew(VoucherRequest voucherRequest);
    void deleteByVoucherCode(String voucherCode);
    void updateByVoucherCode(String voucherCode, VoucherUpdateRequest voucherUpdateRequest);

}
