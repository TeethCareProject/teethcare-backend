package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.mapper.VoucherMapper;
import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherFilterRequest;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.request.VoucherUpdateRequest;
import com.teethcare.model.response.VoucherResponse;
import com.teethcare.service.VoucherService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Voucher.VOUCHER_ENDPOINT)
@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
public class VoucherController {
    private final VoucherService voucherService;
    private final VoucherMapper voucherMapper;

    @PostMapping
    public ResponseEntity<VoucherResponse> add(@Valid @RequestBody VoucherRequest voucherRequest) {
        Voucher voucher = voucherService.addNew(voucherRequest);
        VoucherResponse voucherResponse = voucherMapper.mapVoucherToVoucherResponse(voucher);
        return new ResponseEntity<>(voucherResponse, HttpStatus.OK);
    }

    @GetMapping
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
    public ResponseEntity<VoucherResponse> findByVoucherCode(@PathVariable("voucher-code") String voucherCode) {
        Voucher voucher = voucherService.findByVoucherCode(voucherCode);
        return new ResponseEntity<>(voucherMapper.mapVoucherToVoucherResponse(voucher), HttpStatus.OK);
    }

    @DeleteMapping("/{voucher-code}")
    public ResponseEntity<Message> deleteByVoucherCode(@PathVariable("voucher-code") String voucherCode) {
        voucherService.deleteByVoucherCode(voucherCode);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }

    @PutMapping("/{voucher-code}")
    public ResponseEntity<Message> updateByVoucherCode(@PathVariable("voucher-code") String voucherCode,
                                                       @Valid @RequestBody VoucherUpdateRequest voucherUpdateRequest) {
        voucherService.updateByVoucherCode(voucherCode, voucherUpdateRequest);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
