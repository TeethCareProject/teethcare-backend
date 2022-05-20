package com.teethcare.controller;

import com.teethcare.model.entity.Clinic;
import com.teethcare.service.ClinicServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/clinics")
public class ClinicController {

    @Autowired
    ClinicServiceImp clinicServiceImp;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT')")
    public Collection<Clinic> getAllActiveClinics() {
        return clinicServiceImp.findAllActive();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT', 'MANAGER', 'DENTIST', 'CUSTOMER_SERVICE')")
    public Optional<Clinic> getClinic(@PathVariable("id") int id) {
        return clinicServiceImp.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Clinic> addClinic(@Valid @RequestBody Clinic clinic) {
        return clinicServiceImp.save(clinic);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Clinic> delClinic(@PathVariable("id") int id) {
        return clinicServiceImp.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<Clinic> updateClinic(@PathVariable("id") int id, @Valid @RequestBody Clinic clinic) {
        return clinicServiceImp.update(id, clinic);
    }
}
