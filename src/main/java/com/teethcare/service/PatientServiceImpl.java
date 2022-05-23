package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.model.entity.Patient;
import com.teethcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements CRUDService<Patient> {
    private PatientRepository patientRepository;


    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.getPatientByStatus(Status.ACTIVE.name());
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        return patientRepository.findById(id);
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient delete(Integer id) {
        Optional<Patient> patientData = patientRepository.findById(id);
        if (patientData.isPresent()) {
            Patient patient = patientData.get();
            patient.setStatus(Status.INACTIVE.name());
            return patientRepository.save(patient);
        }
        return null;
    }
}
