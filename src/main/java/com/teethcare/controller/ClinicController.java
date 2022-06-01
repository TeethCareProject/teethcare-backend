package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.CSService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.DentistService;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
    public ResponseEntity<Page<ClinicResponse>> getAllWithFilter(ClinicFilterRequest clinicFilterRequest,
                                                                 @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                 @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                 @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        Page<Clinic> list = clinicService.findAllWithFilter(clinicFilterRequest, pageable);
        Page<ClinicResponse> clinicResponses = list.map(clinicMapper::mapClinicToClinicResponse);
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
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theID = 0;
        if (!NumberUtils.isCreatable(id)) {
            throw new NotFoundException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Clinic clinic = clinicService.findById(theID);
        if (clinic != null) {
            clinic.setStatus(Status.Clinic.INACTIVE.name());
            clinicService.save(clinic);
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

        clinicService.update(clinic);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable String status, @PathVariable String id) {
        for (Status.Clinic eStatus : Status.Clinic.values()) {
            if (eStatus.name().equals(status.toUpperCase())) {
                int theID = ConvertUtils.covertID(id);
                Clinic clinic = clinicService.findById(theID);
                clinic.setStatus(status.toUpperCase());
                clinicService.update(clinic);
                System.out.printf(clinicService.findById(theID).getStatus());
                return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new MessageResponse(Message.INVALID_STATUS.name()), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<List<AccountResponse>> findAllStaffs(@PathVariable String id) {
        int theID = ConvertUtils.covertID(id);

        List<Account> staffList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(theID, Status.Account.ACTIVE.name());
        List<CustomerService> customerServiceList = csService.findByClinicIdAndStatus(theID, Status.Account.ACTIVE.name());

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        List<AccountResponse> staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);

        if (staffResponseList == null || staffResponseList.size() == 0) {
            throw new NotFoundException("With id " + id + ", the list of hospital staff could not be found.");
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<Page<ServiceOfClinicResponse>> getService(ServiceFilterRequest serviceFilterRequest,
                                                                    @RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                                    @PathVariable("id") String id,
                                                                    @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                    @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                    @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);

        int theID = ConvertUtils.covertID(id);

        serviceFilterRequest.setClinicID(theID);

        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            account = jwtTokenUtil.getAccountFromJwt(token);
        }

        Page<ServiceOfClinic> list = serviceOfClinicService.findAllWithFilter(serviceFilterRequest, pageable, account);

        Page<ServiceOfClinicResponse> responses = list.map(new Function<ServiceOfClinic, ServiceOfClinicResponse>() {
            @Override
            public ServiceOfClinicResponse apply(ServiceOfClinic service) {
                return serviceOfClinicMapper.mapServiceOfClinicToServiceOfClinicResponse(service);
            }
        });
        return new ResponseEntity<>(responses, HttpStatus.OK);

    }


}
