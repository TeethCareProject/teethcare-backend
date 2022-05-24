package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.config.model.entity.Clinic;
import com.teethcare.config.model.entity.Manager;
import com.teethcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Clinic> findAllActive() {
        return clinicRepository.getClinicByStatus(Status.ACTIVE.name());
    }

    public Clinic getClinicByManager(Manager manager){
        return clinicRepository.getClinicByManager(manager);
    };
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
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(Status.INACTIVE.name());
            return clinicRepository.save(clinic);
        }
        return null;
    }

}
