package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.ClinicMapper;
import com.teethcare.common.Message;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.config.mapper.ClinicMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.exception.ClinicNotFoundException;
import com.teethcare.service.ClinicService;
import lombok.RequiredArgsConstructor;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CRUDService;
import com.teethcare.service.CSService;
import com.teethcare.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
public class ClinicController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;


    @GetMapping
    @PreAuthorize("hasAnyAuthority((T(com.teethcare.common.Role).ADMIN), T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<List<ClinicResponse>> getAllActiveClinics() {
        List<Clinic> list = clinicService.findAllActive();
        List<ClinicResponse> clinicResponses = clinicMapper.mapClinicListToClinicResponseList(list);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicTmp = clinicService.findById(id);
        if (clinicTmp.isPresent()) {
            ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinicTmp.get());
            return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
        } else {
            throw new ClinicNotFoundException("Clinic id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity delClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicData = clinicService.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(Status.INACTIVE.name());
            return new ResponseEntity( HttpStatus.OK);
        } else {
            throw new ClinicNotFoundException("Clinic id " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody ClinicRequest clinicRequest, @PathVariable int id) {
        clinicRequest.setId(id);
        Clinic clinic = clinicService.findById(id);
        clinicMapper.mapClinicRequestToClinic(clinicRequest);
        clinicService.save(clinic);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public Clinic getClinicById(@PathVariable int id) {
//        Clinic clinic = clinicService.findById(id);
//
//        if (clinic == null) {
//            throw new NotFoundException();
//        }
//
//        return clinic;
//    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<List<AccountResponse>> findAllStaffs(@PathVariable int id) {
        List<Account> staffList = new ArrayList<>();
        List<AccountResponse> staffResponseList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(id, "ACTIVE");
        List<CustomerService> customerServiceList = CSService.findByClinicIdAndStatus(id, "ACTIVE");

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);

        if (staffResponseList == null || staffResponseList.size() == 0) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }
}
