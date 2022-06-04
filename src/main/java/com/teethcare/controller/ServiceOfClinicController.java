package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.model.response.ServiceDetailResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Service.SERVICE_ENDPOINT)
public class ServiceOfClinicController {
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceOfClinicMapper serviceOfClinicMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Page<ServiceOfClinicResponse>> getAll(ServiceFilterRequest serviceFilterRequest,
                                                                @RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                                @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.pagingAndSorting(size, page, field, direction);

        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }
        Page<ServiceOfClinic> listServiceOfClinics = serviceOfClinicService.findAllWithFilter(serviceFilterRequest, pageable, account);

        Page<ServiceOfClinicResponse> responses = listServiceOfClinics.map(serviceOfClinicMapper::mapServiceOfClinicToServiceOfClinicResponse);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDetailResponse> getById(@RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                         @PathVariable("id") int id) {


        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }

        ServiceOfClinic service = serviceOfClinicService.findById(id, account);

        ServiceDetailResponse response = serviceOfClinicMapper.mapServiceOfClinicToServiceDetailResponse(service);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
