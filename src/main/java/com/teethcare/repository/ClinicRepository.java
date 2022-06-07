package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {
    List<Clinic> getClinicByStatus(String Status, Pageable pageable);

    Clinic getClinicByManager(Manager manager);

    List<Clinic> findAllByStatusIsNotNull(Pageable pageable);

    List<Clinic> findAllByStatus(String status, Pageable pageable);

    List<Clinic> findAllByNameContainingIgnoreCaseAndStatus(String search, String status, Pageable pageable);

    List<Clinic> findAllByNameContainingIgnoreCase(String search);
}