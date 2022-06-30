package com.teethcare.service.impl.voucher;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.config.security.UserDetailUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.VoucherMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.request.VoucherUpdateRequest;
import com.teethcare.repository.VoucherRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.VoucherService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;
    private final ClinicService clinicService;


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
    @Transactional
    public Voucher addNew(VoucherRequest voucherRequest) {
        if (voucherRequest.getQuantity() == null && voucherRequest.getExpiredTime() == null) {
            throw new BadRequestException("Both quantity and expired time can not be null");
        }
        Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherRequest.getVoucherCode());
        if (voucher != null) {
            throw new BadRequestException("This voucher code existed!");
        }
        Account account = accountService.getAccountByUsername(UserDetailUtil.getUsername());
        if (account.getRole().getName().equals(Role.ADMIN.name())) {
            return addByAdmin(voucherRequest);
        } else if (account.getRole().getName().equals(Role.MANAGER.name())) {
            return addByManager(voucherRequest, (Manager) account);
        }
        return null;
    }

    @Override
    public Voucher addByAdmin(VoucherRequest voucherRequest) {
        Voucher voucher = voucherMapper.mapVoucherRequestToVoucher(voucherRequest);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (voucher.getExpiredTime().before(now)) {
            throw new BadRequestException("Voucher Expired Time is invalid!");
        }
        save(voucher);
        return voucher;
    }

    @Override
    public Voucher addByManager(VoucherRequest voucherRequest, Manager manager) {
        Voucher voucher = voucherMapper.mapVoucherRequestToVoucher(voucherRequest);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (voucher.getExpiredTime() != null && voucher.getExpiredTime().before(now)) {
            throw new BadRequestException("Voucher Expired Time is invalid!");
        }
        voucher.setClinic(clinicService.getClinicByManager(manager));
        save(voucher);
        return voucher;
    }

    @Override
    public void deleteByVoucherCode(String voucherCode) {
        Account account = accountService.getAccountByUsername(UserDetailUtil.getUsername());
        Voucher voucher = findByVoucherCode(voucherCode);
        if (account.getRole().getName().equals(Role.MANAGER.name()) && !voucher.getClinic().equals(clinicService.getClinicByManager((Manager) account))) {
            throw new BadRequestException("Voucher is not match with this clinic!");
        }
        deactivate(voucher);
    }

    @Override
    public void updateByVoucherCode(String voucherCode, VoucherUpdateRequest voucherUpdateRequest) {
        Account account = accountService.getAccountByUsername(UserDetailUtil.getUsername());
        Voucher voucher = findByVoucherCode(voucherCode);

        if (account.getRole().getName().equals(Role.MANAGER.name()) && !voucher.getClinic().equals(clinicService.getClinicByManager((Manager) account))) {
            throw new BadRequestException("Voucher is not match with this clinic!");
        }
        voucherMapper.updateVoucherFromVoucherUpdateRequest(voucherUpdateRequest, voucher);
    }

    @Override
    public Page<Voucher> findAllWithFilter(VoucherFilterRequest voucherFilterRequest, Pageable pageable) {
        String username = UserDetailUtil.getUsername();
        Account account = accountService.getAccountByUsername(username);
        List<Voucher> vouchers;
        if (account.getRole().getName().equals(Role.ADMIN.name())) {
            vouchers = voucherRepository.findAll();
        } else {
            vouchers = voucherRepository.findAllByClinic(clinicService.getClinicByManager((Manager) account));
        }
        vouchers = vouchers.stream().filter(voucherFilterRequest.getPredicate()).collect(Collectors.toList());
        return PaginationAndSortFactory.convertToPage(vouchers, pageable);
    }

    @Override
    public boolean isAvailable(String voucherCode, Integer clinicId) {
        Voucher voucher = voucherRepository.findVoucherByVoucherCode(voucherCode);
        if (voucher == null) {
            return false;
//            throw new BadRequestException("Voucher is not found!");
        }
        if (clinicId != null && !voucher.getClinic().getId().equals(clinicId)) {
            return false;
//            throw new BadRequestException("Voucher is invalid!");
        }
        long now = System.currentTimeMillis();
        if (voucher.getExpiredTime() != null && voucher.getExpiredTime().getTime() < now) {
            deactivate(voucher);
            return false;
//            throw new BadRequestException("This voucher is expired!");
        }
        if (voucher.getQuantity() != null && !(voucher.getQuantity() > 0)) {
            return false;
//            throw new BadRequestException("This voucher is out of stock!");
        }
        return voucherRepository.findVoucherByVoucherCodeAndStatus(voucherCode, Status.Voucher.AVAILABLE.toString()) != null;
    }

    @Override
    public void useVoucher(String voucherCode, Clinic clinic) {
        if (!isAvailable(voucherCode, clinic == null ? null : clinic.getId())) {
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
