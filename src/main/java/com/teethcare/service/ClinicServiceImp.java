package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class ClinicServiceImp implements ClinicService {
    private ClinicRepository clinicRepository;

    @Autowired
    public ClinicServiceImp(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

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
    public Clinic save(@Valid Clinic clinic) {
        clinic.setId(null);
        return clinicRepository.save(clinic);
    }

    @Override
    public Clinic delete(Integer id) {
        return null;
    }

}
