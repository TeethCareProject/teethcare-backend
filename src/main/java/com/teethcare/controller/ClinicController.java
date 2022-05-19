package com.teethcare.controller;

import com.teethcare.model.entity.Clinic;
import com.teethcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/clinics")
public class ClinicController {
    @Autowired
    private ClinicRepository clinicRepository;

    @GetMapping
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Clinic> getClinic(@PathVariable("id") int id) {
        return clinicRepository.findById(id);
    }

    @PostMapping
    public Clinic addClinic(@RequestBody Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Clinic> delClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        if (clinicData.isPresent()) {
            Clinic nClinic = clinicData.get();
            nClinic.setStatus(false);
            return new ResponseEntity<>(clinicRepository.save(nClinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clinic> updateClinic(@PathVariable("id") int id, @Validated @RequestBody Clinic clinic) {
        if (clinicRepository.findById(id).isPresent()) {
            Clinic nClinic = clinicRepository.findById(id).get();
            nClinic.setManagerId(nClinic.getManagerId());
            nClinic.setLocationId(nClinic.getLocationId());
            nClinic.setName(nClinic.getName());
            nClinic.setDescription(nClinic.getDescription());
            nClinic.setImageUrl(nClinic.getImageUrl());
            nClinic.setTaxCode(nClinic.getTaxCode());
            nClinic.setAvgRatingScore(nClinic.getAvgRatingScore());
            return new ResponseEntity<>(clinicRepository.save(nClinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
