package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Patient;
import com.teethcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Override
    public List<Patient> findAll() {
        return patientRepository.getPatientByStatus(Status.ACTIVE.name());
    }

    @Override
    public Patient findById(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new IdNotFoundException();
        }
        return patient.get();
    }

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public void delete(int id) {
        Optional<Patient> patientData = patientRepository.findById(id);
        Patient patient = patientData.get();
        patient.setStatus(Status.INACTIVE.name());
        patientRepository.save(patient);
    }
}
