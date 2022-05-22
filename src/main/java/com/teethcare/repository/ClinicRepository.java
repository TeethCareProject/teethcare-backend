package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {
    Collection<Clinic> getClinicByStatus(String Status);
    Clinic getClinicByManager(Manager manager);
}