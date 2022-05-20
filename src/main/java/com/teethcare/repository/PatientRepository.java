package com.teethcare.repository;

import com.teethcare.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query(
            value = "SELECT * FROM Patient p WHERE p.status <> 0",
            nativeQuery = true)
    Collection<Patient> findAllActive();
}
