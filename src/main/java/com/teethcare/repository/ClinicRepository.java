package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    @Query(
            value = "SELECT * FROM Clinic c WHERE c.status <> 0",
            nativeQuery = true)
    Collection<Clinic> findAllActive();
}