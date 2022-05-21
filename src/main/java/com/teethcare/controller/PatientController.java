package com.teethcare.controller;

import com.teethcare.model.entity.Patient;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private CRUDService<Patient> patientService;

    public PatientController(CRUDService<Patient> patientService) {
        this.patientService = patientService;
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
    public ResponseEntity addPatient(@Valid @RequestBody Patient patient) {
        Patient addedPatient = patientService.save(patient);
        if (addedPatient != null) {
            return new ResponseEntity<>(addedPatient, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User existed!");
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
