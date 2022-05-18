package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ClinicServiceImp implements ClinicService {
    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public Iterable<Clinic> findAll() {
        return clinicRepository.findAll();
    }
}
