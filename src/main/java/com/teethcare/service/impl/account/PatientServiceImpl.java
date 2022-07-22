package com.teethcare.service.impl.account;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.repository.PatientRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.PatientService;
import com.teethcare.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Patient> findAll() {
        return patientRepository.getPatientByStatus(Status.Account.ACTIVE.name());
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
        patient.setStatus(Status.Account.ACTIVE.name());
        patient.setRole(roleService.getRoleByName(Role.PATIENT.name()));
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
    }

    @Override
    public void delete(int id) {
        Optional<Patient> patientData = patientRepository.findById(id);
        if (patientData.isPresent()) {
            Patient patient = patientData.get();
            patient.setStatus(Status.Account.INACTIVE.name());
            patientRepository.save(patient);
        } else {
            throw new NotFoundException("Patient id " + id + " not found!");
        }
    }

    @Override
    public Patient addNew(PatientRegisterRequest patientRegisterRequest) {
        patientRegisterRequest.trim();
        boolean isDuplicated = accountService.isDuplicated(patientRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (patientRegisterRequest.getPassword().equals(patientRegisterRequest.getConfirmPassword())) {
                Patient patient = accountMapper.mapPatientRegisterRequestToPatient(patientRegisterRequest);
                this.save(patient);
                return patient;
            } else {
                throw new BadRequestException("Confirm Password is not match with password");
            }
        }
        {
            throw new BadRequestException("User existed!");

        }
    }

    @Override
    public void update(Patient theEntity) {

    }
}
