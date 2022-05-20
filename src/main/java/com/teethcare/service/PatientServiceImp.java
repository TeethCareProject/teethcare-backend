package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.Patient;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.ManagerRepository;
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
    private PatientRepository patientRepository;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public PatientServiceImp(PatientRepository patientRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        return patientRepository.findById(id);
    }

    @Override
    public Patient save(@Valid Patient patient) {
        Account account = accountRepository.getAccountByUsernameAndAndStatus(patient.getUsername(), 1);
        if (account == null) {
            patient.setId(null);
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            return patientRepository.save(patient);
        }
        return null;
    }

    @Override
    public Patient delete(Integer id) {
        Optional<Patient> patientData = patientRepository.findById(id);
        if (patientData.isPresent()) {
            Patient patient = patientData.get();
            patient.setStatus(0);
            return patient;
        }
        return null;
    }
}
