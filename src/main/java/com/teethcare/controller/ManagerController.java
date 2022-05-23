package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.CRUDService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableSwagger2
@RequestMapping(path = EndpointConstant.Manager.MANAGER_ENDPOINT)
public class ManagerController {

    private CRUDService<Manager> managerService;
    private AccountService accountService;
    private ClinicService clinicService;
    private LocationService locationService;

    @Autowired
    public ManagerController(CRUDService<Manager> managerService, AccountService accountService,
                             ClinicService clinicService, PasswordEncoder passwordEncoder,
                             LocationService locationService) {
        this.managerService = managerService;
        this.accountService = accountService;
        this.clinicService = clinicService;
        this.passwordEncoder = passwordEncoder;
        this.locationService = locationService;
    }

    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;


    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<ManagerResponse>> getAllManagers() {
        List<Manager> managers = managerService.findAll();
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for (Manager manager : managers) {
            Clinic clinic = clinicService.getClinicByManager(manager);
            managerResponses.add(new ManagerResponse(manager.getId(), manager.getUsername(), manager.getRole().getName(),
                    manager.getFirstName(), manager.getLastName(), manager.getGender(), manager.getEmail(), manager.getPhone(),
                    manager.getStatus(), clinic));
        }
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity addManager(@Valid @RequestBody ManagerRegisterRequest managerRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(managerRegisterRequest.getUsername(), Status.INACTIVE.name());
        if (!isDuplicated) {
            if (managerRegisterRequest.getPassword().equals(managerRegisterRequest.getConfirmPassword())) {
                Manager manager = new Manager();
                manager.setUsername(managerRegisterRequest.getUsername());
                manager.setPassword(managerRegisterRequest.getPassword());
                manager.setFirstName(managerRegisterRequest.getFirstName());
                manager.setLastName(managerRegisterRequest.getLastName());
                manager.setGender(managerRegisterRequest.getGender());
                manager.setRole(new Role(1, "MANAGER"));
                manager.setStatus(Status.PENDING.name());
                manager.setPassword(passwordEncoder.encode(manager.getPassword()));

                Clinic clinic = new Clinic();
                clinic.setManager(manager);
                clinic.setName(managerRegisterRequest.getClinicName());
                clinic.setTaxCode(managerRegisterRequest.getClinicTaxCode());
                Location location = new Location();
                location.setWard(locationService.getWardById(managerRegisterRequest.getWardId()));
                locationService.save(location);
                clinic.setLocation(location);
                clinic.setStatus(Status.PENDING.name());
                managerService.save(manager);
                clinicService.save(clinic);
                ManagerResponse managerResponse = new ManagerResponse(manager.getId(), manager.getUsername(),
                        manager.getRole().getName(), manager.getFirstName(), manager.getLastName(), manager.getGender(),
                        manager.getEmail(), manager.getPhone(), manager.getStatus(), clinic);
                return new ResponseEntity<>(managerResponse, HttpStatus.OK);
            } else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("confirm Password is not match with password");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User existed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<Manager> delManager(@PathVariable("id") Integer id) {
        Manager deletedManager = managerService.delete(id);
        if (deletedManager != null) {
            return new ResponseEntity<>(deletedManager, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
