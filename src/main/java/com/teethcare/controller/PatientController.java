package com.teethcare.controller;

import com.teethcare.model.entity.Patient;
import com.teethcare.service.PatientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientServiceImp patientServiceImp;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Patient> getAllPatients() {
        return patientServiceImp.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT', 'DENTIST', 'CUSTOMER_SERVICE')")
    public Optional<Patient> getPatient(@PathVariable("id") Integer id) {
        return patientServiceImp.findById(id);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity addPatient(@Valid @RequestBody Patient patient) {
        return patientServiceImp.save(patient);
    }

    @DeleteMapping ("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Patient> delPatient(@PathVariable("id") Integer id) {
        return patientServiceImp.delete(id);
    }
}
