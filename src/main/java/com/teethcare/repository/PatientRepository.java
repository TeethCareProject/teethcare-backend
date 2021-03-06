package com.teethcare.repository;

import com.teethcare.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> getPatientByStatus(String status);

}
