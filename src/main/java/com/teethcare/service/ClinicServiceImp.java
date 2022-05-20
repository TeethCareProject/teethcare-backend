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
            clinic.setStatus(false);
            return new ResponseEntity<>(clinicRepository.save(clinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
