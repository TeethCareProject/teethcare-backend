package com.teethcare.config.model.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.config.mapper.ClinicMapper;
import com.teethcare.config.model.entity.Clinic;
import com.teethcare.config.model.entity.Location;
import com.teethcare.config.model.entity.Manager;
import com.teethcare.config.model.request.ManagerRegisterRequest;
import com.teethcare.config.model.response.ClinicInfoResponse;
import com.teethcare.config.model.response.CustomErrorResponse;
import com.teethcare.config.model.response.ManagerResponse;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.exception.RegisterAccountException;
import com.teethcare.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableSwagger2
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
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<ManagerResponse>> getAllManagers() {
        List<Manager> managers = managerService.findAll();
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for (Manager manager : managers) {
            Clinic clinic = clinicService.getClinicByManager(manager);
            if (clinic != null) {
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                managerResponses.add(new ManagerResponse(manager.getId(), manager.getUsername(), manager.getRole().getName(),
                        manager.getFirstName(), manager.getLastName(), manager.getGender(), manager.getEmail(), manager.getPhone(),
                        manager.getStatus(), clinicInfoResponse));
            }
        }
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<ManagerResponse> getActiveManager(@PathVariable("id") int id) {
        Manager manager = managerService.getActiveManager(id);
        Clinic clinic = clinicService.getClinicByManager(manager);
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
        ManagerResponse managerResponse = new ManagerResponse(manager.getId(), manager.getUsername(), manager.getRole().getName(),
                manager.getFirstName(), manager.getLastName(), manager.getGender(), manager.getEmail(), manager.getPhone(),
                manager.getStatus(), clinicInfoResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    @PreAuthorize("permitAll()")
    public ResponseEntity addManager(@Valid @RequestBody ManagerRegisterRequest managerRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(managerRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (managerRegisterRequest.getPassword().equals(managerRegisterRequest.getConfirmPassword())) {
                Manager manager = accountMapper.mapManagerRegisterRequestToManager(managerRegisterRequest);
                manager.setRole(roleService.getRoleByName(com.teethcare.common.Role.PATIENT.name()));

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
                    ManagerResponse managerResponse = new ManagerResponse(manager.getId(), manager.getUsername(),
                            manager.getRole().getName(), manager.getFirstName(), manager.getLastName(), manager.getGender(),
                            manager.getEmail(), manager.getPhone(), manager.getStatus(), clinicInfoResponse);
                    return new ResponseEntity<>(managerResponse, HttpStatus.OK);
                }
            } else
                throw new RegisterAccountException("confirm Password is not match with password");
        }
        throw new RegisterAccountException("User existed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<ManagerResponse> delManager(@PathVariable("id") Integer id) {
        Manager deletedManager = managerService.delete(id);
        if (deletedManager != null) {
            Clinic clinic = clinicService.getClinicByManager(deletedManager);
            clinicService.delete(clinic.getId());
            ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
            ManagerResponse managerResponse = new ManagerResponse(deletedManager.getId(), deletedManager.getUsername(), deletedManager.getRole().getName(),
                    deletedManager.getFirstName(), deletedManager.getLastName(), deletedManager.getGender(), deletedManager.getEmail(), deletedManager.getPhone(),
                    deletedManager.getStatus(), clinicInfoResponse);
            return new ResponseEntity<>(managerResponse, HttpStatus.OK);
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
