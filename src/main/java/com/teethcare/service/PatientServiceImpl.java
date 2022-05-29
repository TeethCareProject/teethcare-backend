package com.teethcare.service;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Patient;
import com.teethcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Patient> findAll() {
        return patientRepository.getPatientByStatus(Status.ACTIVE.name());
    }

    @Override
    public Patient findById(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new NotFoundException("Patient id " + id + " not found!");
        }
        return patient.get();
    }

    @Override
    public void save(Patient patient) {
        patient.setStatus(Status.ACTIVE.name());
        patient.setRole(roleService.getRoleByName(Role.PATIENT.name()));
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
    }

    @Override
    public void delete(int id) {
        Optional<Patient> patientData = patientRepository.findById(id);
        if (patientData.isPresent()) {
            Patient patient = patientData.get();
            patient.setStatus(Status.INACTIVE.name());
            patientRepository.save(patient);
        } else {
            throw new NotFoundException("Patient id " + id + " not found!");
        }
    }
}
