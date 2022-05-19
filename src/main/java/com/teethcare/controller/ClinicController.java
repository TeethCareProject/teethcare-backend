package com.teethcare.controller;

import com.teethcare.model.entity.Clinic;
import com.teethcare.service.ClinicServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/clinics")
public class ClinicController {

    @Autowired
    ClinicServiceImp clinicServiceImp;

    @GetMapping
    public List<Clinic> getAllClinics() {
        return clinicServiceImp.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Clinic> getClinic(@PathVariable("id") int id) {
        return clinicServiceImp.findById(id);
    }

    @PostMapping
    public Clinic addClinic(@Valid @RequestBody Clinic clinic) {
        return clinicServiceImp.save(clinic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Clinic> delClinic(@PathVariable("id") int id) {
        return clinicServiceImp.delete(id);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Clinic> updateClinic(@PathVariable("id") int id, @Validated @RequestBody Clinic clinic) {
//        if (clinicRepository.findById(id).isPresent()) {
//            Clinic nClinic = clinicRepository.findById(id).get();
//            nClinic.setManagerId(nClinic.getManagerId());
//            nClinic.setLocationId(nClinic.getLocationId());
//            nClinic.setName(nClinic.getName());
//            nClinic.setDescription(nClinic.getDescription());
//            nClinic.setImageUrl(nClinic.getImageUrl());
//            nClinic.setTaxCode(nClinic.getTaxCode());
//            nClinic.setAvgRatingScore(nClinic.getAvgRatingScore());
//            return new ResponseEntity<>(clinicRepository.save(nClinic), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
