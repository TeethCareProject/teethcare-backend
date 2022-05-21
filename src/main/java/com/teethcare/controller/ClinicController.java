package com.teethcare.controller;

import com.teethcare.model.entity.Clinic;
import com.teethcare.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/clinics")
public class ClinicController {

    private ClinicService clinicService;

    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT')")
    public ResponseEntity<Collection<Clinic>> getAllActiveClinics() {
        return new ResponseEntity<>(clinicService.findAllActive(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT', 'MANAGER', 'DENTIST', 'CUSTOMER_SERVICE')")
    public ResponseEntity<Optional<Clinic>> getClinic(@PathVariable("id") int id) {
        return new ResponseEntity<>(clinicService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Clinic> addClinic(@Valid @RequestBody Clinic clinic) {
        return new ResponseEntity<>(clinicService.save(clinic), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Clinic> delClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicData = clinicService.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(0);
            return new ResponseEntity<>(clinicService.save(clinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<Clinic> updateClinic(@PathVariable("id") int id, @Valid @RequestBody Clinic clinic) {
        if (clinicService.findById(id).isPresent()) {
            Clinic nClinic = clinicService.findById(id).get();
            nClinic.setManagerId(clinic.getManagerId());
            nClinic.setLocationId(clinic.getLocationId());
            nClinic.setName(clinic.getName());
            nClinic.setDescription(clinic.getDescription());
            nClinic.setImageUrl(clinic.getImageUrl());
            nClinic.setTaxCode(clinic.getTaxCode());
            nClinic.setAvgRatingScore(clinic.getAvgRatingScore());
            return new ResponseEntity(clinicService.save(nClinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
