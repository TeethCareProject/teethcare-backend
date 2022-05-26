package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.config.mapper.ClinicMapper;
import com.teethcare.exception.ClinicNotFoundException;
import com.teethcare.exception.IdInvalidException;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.CustomErrorResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CSService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.DentistService;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.sql.Timestamp;
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
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Clinic clinic = clinicService.findById(theID);
        if (clinic != null) {
            ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
            return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
        } else {
            throw new ClinicNotFoundException("Clinic id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity delClinic(@PathVariable("id") String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Clinic clinic = clinicService.findById(theID);
        if (clinic != null) {
            clinic.setStatus(Status.INACTIVE.name());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new ClinicNotFoundException("Clinic id " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody ClinicRequest clinicRequest, @PathVariable String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new IdInvalidException("Id " + id + " invalid");
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
            throw new IdInvalidException("Id " + id + " invalid");
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
            throw new IdNotFoundException("With id "+ id + ", the list of hospital staff could not be found.");
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List errors = new ArrayList<String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + " " + errorMessage);
        });
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                errors

        );
        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }

}
