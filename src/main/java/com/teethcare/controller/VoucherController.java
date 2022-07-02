package com.teethcare.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.*;
import com.teethcare.config.security.UserDetailUtil;
import com.teethcare.exception.InternalServerError;
import com.teethcare.mapper.VoucherMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.response.AvailableVoucherResponse;
import com.teethcare.model.response.CheckVoucherResponse;
import com.teethcare.model.response.VoucherResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.VoucherService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Voucher.VOUCHER_ENDPOINT)
public class VoucherController {
    private final VoucherService voucherService;
    private final VoucherMapper voucherMapper;
    private final AccountService accountService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final ClinicService clinicService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<VoucherResponse> add(@Valid @RequestBody VoucherRequest voucherRequest) {
        Voucher voucher = voucherService.addNew(voucherRequest);
        VoucherResponse voucherResponse = voucherMapper.mapVoucherToVoucherResponse(voucher);
        return new ResponseEntity<>(voucherResponse, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<Page<VoucherResponse>> findAll(VoucherFilterRequest voucherFilterRequest,
                                                         @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                         @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                         @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<Voucher> vouchers = voucherService.findAllWithFilter(voucherFilterRequest, pageable);
        Page<VoucherResponse> voucherResponses = vouchers.map(voucherMapper::mapVoucherToVoucherResponse);
        return new ResponseEntity<>(voucherResponses, HttpStatus.OK);
    }

    @GetMapping("/{voucher-code}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER, T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<VoucherResponse> findActiveByVoucherCode(@PathVariable("voucher-code") String voucherCode) {
        Voucher voucher = voucherService.findActiveByVoucherCode(voucherCode);
        return new ResponseEntity<>(voucherMapper.mapVoucherToVoucherResponse(voucher), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER, T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<VoucherResponse> findVoucherById(@PathVariable("id") int voucherId) {
        Voucher voucher = voucherService.findVoucherById(voucherId);
        return new ResponseEntity<>(voucherMapper.mapVoucherToVoucherResponse(voucher), HttpStatus.OK);
    }

    @DeleteMapping("/{voucher-code}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<Message> deleteByVoucherCode(@PathVariable("voucher-code") String voucherCode) {
        Account account = accountService.getAccountByUsername(UserDetailUtil.getUsername());
        Voucher voucher = voucherService.findActiveByVoucherCode(voucherCode);
        voucherService.deleteByVoucherCode(voucherCode);
        if (account.getRole().getName().equals(Role.ADMIN.name()) && voucher.getClinic() != null && voucher.getClinic().equals(clinicService.getClinicByManager((Manager) account))) {
            try {
                firebaseMessagingService.sendNotificationToManagerByClinic(voucher.getClinic(), NotificationType.DELETE_VOUCHER.name(),
                        NotificationMessage.DELETE_VOUCHER + voucherCode);
                log.info("Successful notification");
            } catch (FirebaseMessagingException ex) {
                throw new InternalServerError("Error while sending notification");
            }
        }
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }

    @GetMapping("/{voucher-code}/check-available")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER, T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<Object> checkAvailable(@PathVariable("voucher-code") String voucherCode,
                                                 @RequestParam Integer clinicId) {
        boolean check = voucherService.isAvailable(voucherCode, clinicId);
        Voucher voucher = voucherService.findActiveByVoucherCode(voucherCode);
        Long expiredTime = null;
        if (voucher.getExpiredTime() != null) {
            expiredTime = voucher.getExpiredTime().getTime();
        }
        return check
                ? new ResponseEntity<>(new AvailableVoucherResponse(voucher.getVoucherCode(), expiredTime, voucher.getDiscountValue()), HttpStatus.OK)
                : new ResponseEntity<>(new CheckVoucherResponse(Status.Voucher.UNAVAILABLE.name()), HttpStatus.BAD_REQUEST);
    }
}
