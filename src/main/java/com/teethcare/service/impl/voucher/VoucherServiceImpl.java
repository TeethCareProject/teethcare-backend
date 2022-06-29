package com.teethcare.service.impl.voucher;

import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher findById(int id) {
        return null;
    }

    @Override
    public void save(Voucher entity) {
        entity.setStatus(Status.Voucher.AVAILABLE.name());
        entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        voucherRepository.save(entity);
    }

    @Override
    public void delete(int id) {
        // NOT USE
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
        voucher.setStatus(Status.Voucher.UNAVAILABLE.name());
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

    @Override
    public boolean isAvailable(String voucherCode) {
        Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode);
        if (voucher == null) {
            throw new BadRequestException("Voucher is not found!");
        }
        Long now = System.currentTimeMillis();
        if (voucher.getExpiredTime() != null) {
            if (voucher.getExpiredTime().getTime() < now) {
                deactivate(voucher);
                throw new BadRequestException("This voucher is expired!");
            }
        }
        if (voucher.getQuantity() != null) {
            if (!(voucher.getQuantity() > 0)) {
                deactivate(voucher);
                throw new BadRequestException("This voucher is out of stock!");
            }
        }
        return voucherRepository.findVoucherByVoucherCodeAndStatus(voucherCode, Status.Voucher.AVAILABLE.toString()) != null;
    }

    @Override
    public void useVoucher(String voucherCode) {
        if (!isAvailable(voucherCode)) {
            throw new BadRequestException("This voucher is not available");
        }
        Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode);
        Integer quantity = voucher.getQuantity();
        if (quantity != null && quantity > 0) {
            quantity--;
            voucher.setQuantity(quantity);
            if (quantity == 0) {
                deactivate(voucher);
            }
        }
    }

    @Override
    public void deactivate(Voucher voucher) {
        voucher.setStatus(Status.Voucher.UNAVAILABLE.toString());
        update(voucher);
    }

    @Override
    public List<Voucher> findAllVouchersByExpiredTime(long expiredTime) {
        return voucherRepository.findAllByExpiredTime(new Timestamp(expiredTime));
    }
}
