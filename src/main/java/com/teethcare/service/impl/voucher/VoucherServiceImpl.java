package com.teethcare.service.impl.voucher;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.VoucherMapper;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.request.VoucherUpdateRequest;
import com.teethcare.repository.VoucherRepository;
import com.teethcare.service.VoucherService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    @Override
    public List<Voucher> findAll() {
        return null;
    }

    @Override
    public Voucher findById(int id) {
        return null;
    }

    @Override
    public void save(Voucher entity) {
        entity.setStatus(Status.Voucher.ACTIVE.name());
        entity.setCreatedTime(System.currentTimeMillis());
        voucherRepository.save(entity);
    }

    @Override
    public void delete(int id) {
    }

    @Override
    public void update(Voucher entity) {
        voucherRepository.save(entity);
    }

    @Override
    public Voucher findByVoucherCode(String voucherCode) {
        Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode);
        if (voucher == null) {
            throw new NotFoundException("Voucher Code " + voucherCode + " not found!");
        }
        return voucher;
    }

    @Override
    public Voucher addNew(VoucherRequest voucherRequest) {
        Voucher voucher = voucherMapper.mapVoucherRequestToVoucher(voucherRequest);
        save(voucher);
        return voucher;
    }

    @Override
    public void deleteByVoucherCode(String voucherCode) {
        Voucher voucher = findByVoucherCode(voucherCode);
        voucher.setStatus(Status.Voucher.INACTIVE.name());
        update(voucher);
    }

    @Override
    public void updateByVoucherCode(String voucherCode, VoucherUpdateRequest voucherUpdateRequest) {
        Voucher voucher = findByVoucherCode(voucherCode);
        voucherMapper.updateVoucherFromVoucherUpdateRequest(voucherUpdateRequest, voucher);
    }

    @Override
    public Page<Voucher> findAllWithFilter(VoucherFilterRequest voucherFilterRequest, Pageable pageable) {
        List<Voucher> vouchers = findAll();
        vouchers = vouchers.stream().filter(voucherFilterRequest.getPredicate()).collect(Collectors.toList());
        return PaginationAndSortFactory.convertToPage(vouchers, pageable);
    }
}
