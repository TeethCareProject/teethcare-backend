package com.teethcare.controller;

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
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CSService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.DentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
public class ClinicController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final CSService csService;
    private final DentistService dentistService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<ClinicResponse>> getAllActiveClinics() {
        List<Clinic> list = clinicService.findAllActive();
        List<ClinicResponse> clinicResponses = clinicMapper.mapClinicListToClinicResponseList(list);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
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
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Clinic clinic = clinicService.findById(theID);
        if (clinic != null) {
            clinic.setStatus(Status.INACTIVE.name());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new NotFoundException("Clinic id " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody ClinicRequest clinicRequest, @PathVariable String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        clinicRequest.setId(theID);

        Clinic clinic = clinicService.findById(theID);

        clinicMapper.mapClinicRequestToClinic(clinicRequest);

        clinicService.save(clinic);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<List<AccountResponse>> findAllStaffs(@PathVariable String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);

        List<Account> staffList = new ArrayList<>();
        List<AccountResponse> staffResponseList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(theID, "ACTIVE");
        List<CustomerService> customerServiceList = csService.findByClinicIdAndStatus(theID, "ACTIVE");

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);

        if (staffResponseList == null || staffResponseList.size() == 0) {
            throw new NotFoundException("With id "+ id + ", the list of hospital staff could not be found.");
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }


}
