package com.teethcare.repository;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentistRepository extends JpaRepository<Dentist, Integer> {
    List<Dentist> findByClinicId(int id);
    List<Dentist> findByClinicIdAndStatus(int id, String status);
}
