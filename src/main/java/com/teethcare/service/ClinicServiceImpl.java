package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }


    public List<Clinic> findAllActive() {
        return clinicRepository.getClinicByStatus(Status.ACTIVE.name());
    }

    public Clinic getClinicByManager(Manager manager) {
        return clinicRepository.getClinicByManager(manager);
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
    public List<Clinic> findAllPendingClinic() {
        return clinicRepository.getClinicByStatus(Status.PENDING.name());
    }

    @Override
    public void save(Clinic clinic) {
        clinicRepository.save(clinic);
    }

    @Override
    public void delete(int id) {
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        Clinic clinic = clinicData.get();
        clinic.setStatus(Status.INACTIVE.name());
        clinicRepository.save(clinic);
    }

}
