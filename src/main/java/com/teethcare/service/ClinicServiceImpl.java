package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class ClinicServiceImpl implements ClinicService {
    private ClinicRepository clinicRepository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }

    public Collection<Clinic> findAllActive() {
        return clinicRepository.getClinicByStatusIsNot(0);
    }

    @Override
    public Optional<Clinic> findById(Integer id) {
        return clinicRepository.findById(id);
    }

    @Override
    public Clinic save(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    @Override
    public Clinic delete(Integer id) {
        return null;
    }

}
