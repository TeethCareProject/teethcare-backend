package com.teethcare.service;

import com.teethcare.model.entity.Clinic;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements CRUDService<Clinic>{

    private ClinicRepository clinicRepository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<Clinic> findAll() {
        return null;
    }

    @Override
    public Clinic findById(int theId) {
        Optional<Clinic> result = clinicRepository.findById(theId);

        Clinic theClinic = null;

        if (result.isPresent()) {
            theClinic = result.get();
        }

        return theClinic;
    }

    @Override
    public void save(Clinic theClinic) {
        clinicRepository.save(theClinic);
    }

    @Override
    public void deleteById(int theId) {

    }
}
