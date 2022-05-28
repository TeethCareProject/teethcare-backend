package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.config.mapper.ClinicMapper;
import com.teethcare.exception.IdInvalidException;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.exception.RegisterAccountException;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.CustomErrorResponse;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Manager.MANAGER_ENDPOINT)
public class ManagerController {

    private final ManagerService managerService;
    private final AccountService accountService;
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final AccountMapper accountMapper;
    private final LocationService locationService;
    private final WardService wardService;


    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<ManagerResponse>> getAll() {
        List<Manager> managers = managerService.findAll();
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for (Manager manager : managers) {
            Clinic clinic = clinicService.getClinicByManager(manager);
            if (clinic != null) {
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                managerResponses.add(accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse));
            }
        }
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<ManagerResponse> getActive(@PathVariable("id") String id) {
        int theID = 0;
        if (!NumberUtils.isCreatable(id)) {
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Manager manager = managerService.getActiveManager(theID);
        Clinic clinic = clinicService.getClinicByManager(manager);
        if (clinic != null) {
            ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
            ManagerResponse managerResponse = accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse);
            return new ResponseEntity<>(managerResponse, HttpStatus.OK);
        } else {
            throw new IdNotFoundException("Account id " + id + " not found!");
        }
    }

    @PostMapping
    @Transactional
    @PreAuthorize("permitAll()")
    public ResponseEntity<ManagerResponse> add(@Valid @RequestBody ManagerRegisterRequest managerRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(managerRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (managerRegisterRequest.getPassword().equals(managerRegisterRequest.getConfirmPassword())) {
                Manager manager = accountMapper.mapManagerRegisterRequestToManager(managerRegisterRequest);
                Clinic clinic = clinicMapper.mapManagerRegisterRequestListToClinic(managerRegisterRequest);
                Location location = new Location();
                location.setWard(wardService.findById(managerRegisterRequest.getWardId()));
                location.setAddressString(managerRegisterRequest.getClinicAddress());
                locationService.save(location);
                managerService.save(manager);
                clinicService.saveWithManagerAndLocation(clinic, manager, location);
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                ManagerResponse managerResponse = accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse);
                return new ResponseEntity<>(managerResponse, HttpStatus.OK);
            } else {
                throw new RegisterAccountException("confirm Password is not match with password");
            }
        }
        throw new RegisterAccountException("User existed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theID = 0;
        if (!NumberUtils.isCreatable(id)) {
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Manager manager = managerService.findById(theID);
        if (manager != null) {
            managerService.delete(theID);
            Clinic clinic = clinicService.getClinicByManager(manager);
            clinicService.delete(clinic.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new IdNotFoundException("Manager id " + id + " not found!");
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
