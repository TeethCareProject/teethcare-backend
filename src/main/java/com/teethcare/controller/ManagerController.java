package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @GetMapping
    //@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<ManagerResponse>> getAllManagers() {
        List<Manager> managers = managerService.findAll();
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for (Manager manager : managers) {
            Clinic clinic = clinicService.getClinicByManager(manager);
            if (clinic != null) {
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                managerResponses.add(new ManagerResponse(manager.getId(), manager.getUsername(), manager.getRole().getId(), manager.getRole().getName(),
                        manager.getFirstName(), manager.getLastName(), manager.getGender(), manager.getEmail(), manager.getPhone(),
                        manager.getStatus(), clinicInfoResponse));
            }
        }
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    //@PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity getActiveManager(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Manager manager = managerService.getActiveManager(theID);
        Clinic clinic = clinicService.getClinicByManager(manager);
        if (clinic != null) {
            ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
            ManagerResponse managerResponse = new ManagerResponse(manager.getId(), manager.getUsername(), manager.getRole().getId(), manager.getRole().getName(),
                    manager.getFirstName(), manager.getLastName(), manager.getGender(), manager.getEmail(), manager.getPhone(),
                    manager.getStatus(), clinicInfoResponse);

            return new ResponseEntity<>(managerResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Manager not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @Transactional
    //@PreAuthorize("permitAll()")
    public ResponseEntity addManager(@Valid @RequestBody ManagerRegisterRequest managerRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(managerRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (managerRegisterRequest.getPassword().equals(managerRegisterRequest.getConfirmPassword())) {
                Manager manager = accountMapper.mapManagerRegisterRequestToManager(managerRegisterRequest);
                manager.setRole(roleService.getRoleByName(Role.PATIENT.name()));

                manager.setStatus(Status.PENDING.name());
                manager.setPassword(passwordEncoder.encode(manager.getPassword()));

                Clinic clinic = clinicMapper.mapManagerRegisterRequestListToClinic(managerRegisterRequest);
                clinic.setManager(manager);
                Location location = new Location();
                location.setWard(locationService.getWardById(managerRegisterRequest.getWardId()));
                location.setAddressString(managerRegisterRequest.getClinicAddress());
                if (location != null) {
                    locationService.save(location);
                    clinic.setLocation(location);
                    clinic.setStatus(Status.PENDING.name());
                    managerService.save(manager);
                    clinicService.save(clinic);
                    ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                    ManagerResponse managerResponse = new ManagerResponse(manager.getId(), manager.getUsername(), manager.getRole().getId(), manager.getRole().getName(),
                            manager.getFirstName(), manager.getLastName(), manager.getGender(), manager.getEmail(), manager.getPhone(),
                            manager.getStatus(), clinicInfoResponse);
                    return new ResponseEntity<>(managerResponse, HttpStatus.OK);
                }
            } else
                throw new BadRequestException("confirm Password is not match with password");
        }
        throw new BadRequestException("User existed!");
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity delManager(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Manager manager = managerService.findById(theID);
        if (manager != null) {
            managerService.delete(theID);
            Clinic clinic = clinicService.getClinicByManager(manager);
            clinicService.delete(clinic.getId());
            return new ResponseEntity(HttpStatus.OK);
        }
        throw new NotFoundException("Manager id " + id + " not found!");
    }

}
