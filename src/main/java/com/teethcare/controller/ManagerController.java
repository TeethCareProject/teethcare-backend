package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.service.*;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<ManagerResponse> getActive(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Manager manager = managerService.getActiveManager(theID);
        Clinic clinic = clinicService.getClinicByManager(manager);
        if (clinic != null) {
            ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
            ManagerResponse managerResponse = accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse);
            return new ResponseEntity<>(managerResponse, HttpStatus.OK);
        } else {
            throw new NotFoundException("Account id " + id + " not found!");
        }
    }

    @PostMapping
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
                throw new BadRequestException("confirm Password is not match with password");
            }
        }
        throw new BadRequestException("User existed!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Manager manager = managerService.findById(theID);
        if (manager != null) {
            managerService.delete(theID);
            Clinic clinic = clinicService.getClinicByManager(manager);
            clinicService.delete(clinic.getId());
            return new ResponseEntity<>("Delete successfuly.",HttpStatus.OK);
        }
        throw new NotFoundException("Manager id " + id + " was not found!");
    }

}
