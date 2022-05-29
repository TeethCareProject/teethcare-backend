package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.CustomErrorResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CSService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.DentistService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
public class ClinicController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final CSService csService;
    private final DentistService dentistService;
    private final AccountMapper accountMapper;


    @GetMapping
    public ResponseEntity<List<ClinicResponse>> getAllActive(@RequestBody Optional<ClinicFilterRequest> clinicFilterRequest,
                                                             @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                             @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                             @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                             @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        List<Clinic> list = clinicService.findAllActive(pageable);
        if (clinicFilterRequest.isPresent()) {
            ClinicFilterRequest filter = clinicFilterRequest.get();
            if (filter.getSearchKey() != null) {
                list = clinicService.searchAllActiveByName(filter.getSearchKey(), pageable);
            }
            if (filter.getProvinceId() != null) {
                Predicate<Clinic> byProvinceId = (clinic) -> clinic.getLocation().getWard().getDistrict().getProvince().getId() == filter.getProvinceId();
                list = list.stream().filter(byProvinceId).collect(Collectors.toList());
            }
            if (filter.getDistrictId() != null) {
                Predicate<Clinic> byDistrictId = (clinic) -> clinic.getLocation().getWard().getDistrict().getId() == filter.getWardId();
                list = list.stream().filter(byDistrictId).collect(Collectors.toList());
            }
            if (filter.getWardId() != null) {
                Predicate<Clinic> byWardId = (clinic) -> clinic.getLocation().getWard().getId() == filter.getWardId();
                list = list.stream().filter(byWardId).collect(Collectors.toList());
            }
        }
        List<ClinicResponse> clinicResponses = clinicMapper.mapClinicListToClinicResponseList(list);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Clinic clinic = clinicService.findById(theID);
        if (clinic != null) {
            ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
            return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
        } else {
            throw new NotFoundException("Clinic id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity delClinic(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theID = 0;
        if (!NumberUtils.isCreatable(id)) {
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Clinic clinic = clinicService.findById(theID);
        if (clinic != null) {
            clinic.setStatus(Status.INACTIVE.name());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new NotFoundException("Clinic id " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody ClinicRequest clinicRequest, @PathVariable String id) {
        int theID = ConvertUtils.covertID(id);
        clinicRequest.setId(theID);

        Clinic clinic = clinicService.findById(theID);

        clinicMapper.mapClinicRequestToClinic(clinicRequest);

        clinicService.save(clinic);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<List<AccountResponse>> findAllStaffs(@PathVariable String id) {
        int theID = ConvertUtils.covertID(id);

        List<Account> staffList = new ArrayList<>();
        List<AccountResponse> staffResponseList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(theID, Status.ACTIVE.name());
        List<CustomerService> customerServiceList = csService.findByClinicIdAndStatus(theID, Status.ACTIVE.name());

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        List<AccountResponse> staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);

        if (staffResponseList == null || staffResponseList.size() == 0) {
            throw new NotFoundException("With id "+ id + ", the list of hospital staff could not be found.");
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Clinic>> getAllPending(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                      @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                      @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction){

        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        List<Clinic> clinicList = clinicService.findAllPendingClinic();
        return new ResponseEntity<>(clinicList, HttpStatus.OK);
    }


}
