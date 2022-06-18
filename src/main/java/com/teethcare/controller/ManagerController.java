package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.*;
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
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final AccountMapper accountMapper;


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
    public ResponseEntity<ManagerResponse> getActive(@PathVariable("id") int id) {
        Manager manager = managerService.getActiveManager(id);
        Clinic clinic = clinicService.getClinicByManager(manager);
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
        ManagerResponse managerResponse = accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ManagerResponse> add(@Valid @RequestBody ManagerRegisterRequest managerRegisterRequest) {
        ManagerResponse managerResponse = managerService.addNew(managerRegisterRequest);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable("id") int id) {
        Manager manager = managerService.findById(id);
        managerService.delete(id);
        Clinic clinic = clinicService.getClinicByManager(manager);
        clinicService.delete(clinic.getId());
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);

    }

}
