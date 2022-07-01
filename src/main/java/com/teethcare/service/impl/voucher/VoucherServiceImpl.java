package com.teethcare.service.impl.voucher;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.NotificationMessage;
import com.teethcare.common.NotificationType;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.UserDetailUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.InternalServerError;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.VoucherMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.repository.VoucherRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.VoucherService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final AccountService accountService;
    private final ClinicService clinicService;
    private final FirebaseMessagingService firebaseMessagingService;


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
    public Voucher findActiveByVoucherCode(String voucherCode) {
        Voucher voucher = voucherRepository.findVoucherByVoucherCodeAndStatus(voucherCode, Status.Voucher.AVAILABLE.name());
        if (voucher == null) {
            throw new NotFoundException("Voucher Code " + voucherCode + " not found!");
        }
        return voucher;
    }

    @Override
    @Transactional
    public Voucher addNew(VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findVoucherByVoucherCodeAndStatus(voucherRequest.getVoucherCode(), Status.Voucher.AVAILABLE.name());
        if (voucher != null) {
            throw new BadRequestException("This voucher code existed!");
        }
        Voucher voucherTmp = voucherMapper.mapVoucherRequestToVoucher(voucherRequest);
        if (voucherRequest.getExpiredTime() != null) {
            voucherTmp.setExpiredTime(new Timestamp(voucherRequest.getExpiredTime()));
        }
        Account account = accountService.getAccountByUsername(UserDetailUtil.getUsername());
        if (account.getRole().getName().equals(Role.ADMIN.name())) {
            return addByAdmin(voucherTmp);
        } else if (account.getRole().getName().equals(Role.MANAGER.name())) {
            return addByManager(voucherTmp, (Manager) account);
        }
        return null;
    }

    @Override
    public Voucher addByAdmin(Voucher voucher) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (voucher.getExpiredTime() != null && voucher.getExpiredTime().before(now)) {
            throw new BadRequestException("Voucher Expired Time is invalid!");
        }
        if (voucher.getQuantity() != null && voucher.getQuantity() <= 0) {
            throw new BadRequestException("Voucher Quantity is invalid!");
        }
        save(voucher);
        return voucher;
    }


    @Override
    public Voucher addByManager(Voucher voucher, Manager manager) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (voucher.getExpiredTime() != null && voucher.getExpiredTime().before(now)) {
            throw new BadRequestException("Voucher Expired Time is invalid!");
        }
        if (voucher.getQuantity() != null && voucher.getQuantity() <= 0) {
            throw new BadRequestException("Voucher Quantity is invalid!");
        }
        voucher.setClinic(clinicService.getClinicByManager(manager));
        save(voucher);
        return voucher;
    }

    @Override
    public void deleteByVoucherCode(String voucherCode) {
        Account account = accountService.getAccountByUsername(UserDetailUtil.getUsername());
        Voucher voucher = findActiveByVoucherCode(voucherCode);
        if (account.getRole().getName().equals(Role.MANAGER.name()) && voucher.getClinic() != null && !voucher.getClinic().equals(clinicService.getClinicByManager((Manager) account))) {
            throw new BadRequestException("Voucher is not match with this clinic!");
        }
        if (account.getRole().getName().equals(Role.MANAGER.name()) && voucher.getClinic() != null && voucher.getClinic().equals(clinicService.getClinicByManager((Manager) account))) {
            try {
                firebaseMessagingService.sendNotificationToManagerByClinic(voucher.getClinic(), NotificationType.DELETE_VOUCHER.name(),
                        NotificationMessage.DELETE_VOUCHER + voucherCode);
                log.info("Successful notification");
            } catch (FirebaseMessagingException ex) {
                throw new InternalServerError("Error while sending notification");
            }
        }
        deactivate(voucher);
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
        Voucher voucher = findActiveByVoucherCode(voucherCode);
        if (voucher == null) {
            return false;
//            throw new BadRequestException("Voucher is not found!");
        }
        if (clinicId != null && voucher.getClinic() != null && !voucher.getClinic().getId().equals(clinicId)) {
            return false;
//            throw new BadRequestException("Voucher is invalid!");
        }
        long now = System.currentTimeMillis();
        if (voucher.getExpiredTime() != null && voucher.getExpiredTime().getTime() < now) {
            disable(voucher);
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
        Voucher voucher = findActiveByVoucherCode(voucherCode);
        Integer quantity = voucher.getQuantity();
        if (quantity != null && quantity > 0) {
            quantity--;
            voucher.setQuantity(quantity);
            if (quantity == 0) {
                disable(voucher);
            }
        }
    }

    @Override
    public void deactivate(Voucher voucher) {
        voucher.setStatus(Status.Voucher.INACTIVE.toString());
        update(voucher);
    }


    @Override
    public void disable(Voucher voucher) {
        voucher.setStatus(Status.Voucher.UNAVAILABLE.toString());
        update(voucher);
    }

    @Override
    public List<Voucher> findAllVouchersByExpiredTime(long expiredTime) {
        return voucherRepository.findAllByExpiredTime(new Timestamp(expiredTime));
    }
}
