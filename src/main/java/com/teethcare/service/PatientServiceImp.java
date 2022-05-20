package com.teethcare.service;

import com.teethcare.model.entity.Patient;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImp implements CRUDService<Patient> {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        return patientRepository.findById(id);
    }

    @Override
    public ResponseEntity save(@Valid Patient patient) {
        String username = accountRepository.getActiveUserName(patient.getUsername());
        if (username == null) {
            patient.setId(null);
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            return new ResponseEntity<>(patientRepository.save(patient), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User existed!");

    }

    @Override
    public ResponseEntity delete(Integer id) {
        Optional<Patient> patientData = patientRepository.findById(id);
        if (patientData.isPresent()) {
            Patient patient = patientData.get();
            patient.setStatus(0);
            return new ResponseEntity<>(patientRepository.save(patient), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
