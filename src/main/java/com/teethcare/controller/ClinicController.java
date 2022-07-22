package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.ForbiddenException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.exception.UnauthorizedException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.request.ClinicStatisticRequest;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.ClinicStatisticResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.*;
import com.teethcare.utils.PaginationAndSortFactory;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.teethcare.utils.PaginationAndSortFactory;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
public class ClinicController {

    private final AccountService accountService;
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final CSService csService;
    private final DentistService dentistService;
    private final AccountMapper accountMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceOfClinicMapper serviceOfClinicMapper;


    @GetMapping
    public ResponseEntity<Page<ClinicResponse>> getAllWithFilter(ClinicFilterRequest clinicFilterRequest,
                                                                 @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                 @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                 @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<Clinic> list = clinicService.findAllWithFilter(clinicFilterRequest, pageable);
        Page<ClinicResponse> clinicResponses = list.map(clinicMapper::mapClinicToClinicResponse);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") int id) {
        Clinic clinic = clinicService.findById(id);
        ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        Clinic clinic = clinicService.findById(id);
        clinic.setStatus(Status.Clinic.INACTIVE.name());
        clinicService.save(clinic);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<ClinicResponse> update(@Valid @RequestBody ClinicRequest clinicRequest,
                                                  @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Clinic clinic = clinicService.updateProfile(clinicRequest, username);
        ClinicResponse response = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/update-image")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<ClinicResponse> updateImage(@RequestBody MultipartFile image,
                                                 @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Clinic clinic = clinicService.updateImage(image, username);
        ClinicResponse response = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/staffs")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<Page<AccountResponse>> findAllStaffs (@PathVariable int id,
                                                                @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);

        List<Account> staffList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(id, Status.Account.ACTIVE.name());
        List<CustomerService> customerServiceList = csService.findByClinicIdAndStatus(id, Status.Account.ACTIVE.name());

        if (!dentistList.isEmpty()) {
            staffList.addAll(dentistList);
        }
        if (!customerServiceList.isEmpty()) {
            staffList.addAll(customerServiceList);
        }
        List<AccountResponse> staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);
        Page<AccountResponse> responses = PaginationAndSortFactory.convertToPage(staffResponseList, pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<Page<ServiceOfClinicResponse>> getService(ServiceFilterRequest serviceFilterRequest,
                                                                    @RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                                    @PathVariable("id") int id,
                                                                    @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                    @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                    @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);


        serviceFilterRequest.setClinicID(id);

        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }

        Page<ServiceOfClinic> list = serviceOfClinicService.findAllWithFilter(serviceFilterRequest, pageable, account);

        Page<ServiceOfClinicResponse> responses = list.map(serviceOfClinicMapper::mapServiceOfClinicToServiceOfClinicResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);

    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ClinicResponse> approve(@PathVariable("id") int id) throws MessagingException {
        Clinic clinic = clinicService.findById(id);
        clinic = clinicService.approve(clinic);
        ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ClinicResponse> reject(@PathVariable("id") int id) throws MessagingException {
        Clinic clinic = clinicService.findById(id);
        clinic = clinicService.reject(clinic);
        ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}/facebookPageId")
    public ResponseEntity<String> getFacebookPageId(@PathVariable String id) {
        String facebookPageId = clinicService.findFacebookPageIdByClinicId(id);
        if (facebookPageId == null) {
            return ResponseEntity.ok("{\"facebookPageId\": null}");
        }
        return ResponseEntity.ok("{\"facebookPageId\": \""+facebookPageId+"\"}");
    }

    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).MANAGER, T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<ClinicStatisticResponse> statistic(@PathVariable("id") int id,
                                                             ClinicStatisticRequest clinicStatisticRequest,
                                                             @RequestHeader(value = AUTHORIZATION) String token){
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Account account = accountService.getAccountByUsername(username);
        Clinic clinic = clinicService.findById(id);
        if (account.getRole().getName().equals(Role.MANAGER.name()) && account.getId().compareTo(clinic.getManager().getId()) != 0){
            throw new UnauthorizedException("Cannot access this clinic statistic.");
        }
        ClinicStatisticResponse response = clinicService.statistic(clinicStatisticRequest, clinic);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
