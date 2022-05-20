package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImp implements CRUDService<Clinic> {
    @Autowired
    ClinicRepository clinicRepository;

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }

    public Collection<Clinic> findAllActive() {
        return clinicRepository.findAllActive();
    }

    @Override
    public Optional<Clinic> findById(Integer id) {
        return clinicRepository.findById(id);
    }

    @Override
    public ResponseEntity save(@Valid Clinic clinic) {
        clinic.setId(null);
        return new ResponseEntity<>(clinicRepository.save(clinic), HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(Integer id) {
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(0);
            return new ResponseEntity<>(clinicRepository.save(clinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity update(Integer id, Clinic clinic) {
        if (clinicRepository.findById(id).isPresent()) {
            Clinic nClinic = clinicRepository.findById(id).get();
            nClinic.setManagerId(clinic.getManagerId());
            nClinic.setLocationId(clinic.getLocationId());
            nClinic.setName(clinic.getName());
            nClinic.setDescription(clinic.getDescription());
            nClinic.setImageUrl(clinic.getImageUrl());
            nClinic.setTaxCode(clinic.getTaxCode());
            nClinic.setAvgRatingScore(clinic.getAvgRatingScore());
            return new ResponseEntity(clinicRepository.save(nClinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
