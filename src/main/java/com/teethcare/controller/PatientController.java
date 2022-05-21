package com.teethcare.controller;

import com.teethcare.common.Status;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.Role;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.service.AccountService;
import com.teethcare.service.CRUDService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/patients")
public class PatientController {

    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;
    private CRUDService<Patient> patientService;
    private AccountService accountService;

    public PatientController(CRUDService<Patient> patientService, PasswordEncoder passwordEncoder,
                             ModelMapper mapper, AccountService accountService) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.patientService = patientService;
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return new ResponseEntity<>(patientService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT', 'DENTIST', 'CUSTOMER_SERVICE')")
    public ResponseEntity<Optional<Patient>> getPatient(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(patientService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity addPatient(@Valid @RequestBody PatientRegisterRequest patientRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(patientRegisterRequest.getUsername(), Status.INACTIVE.name());
        if (!isDuplicated) {
            Patient patient = mapper.map(patientRegisterRequest, Patient.class);
            patient.setRole(new Role(3, "PATIENT"));
            patient.setStatus(Status.ACTIVE.name());
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            Patient addedPatient = patientService.save(patient);
            return new ResponseEntity<>(addedPatient, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User existed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Patient> delPatient(@PathVariable("id") Integer id) {
        Patient deletedPatient = patientService.delete(id);
        if (deletedPatient != null) {
            return new ResponseEntity<>(deletedPatient, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
