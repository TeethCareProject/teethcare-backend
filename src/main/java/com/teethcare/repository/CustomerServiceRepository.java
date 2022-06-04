package com.teethcare.repository;

import com.teethcare.model.entity.CustomerService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Integer> {
    List<CustomerService> findByClinicId(int id);

    List<CustomerService> findByClinicIdAndStatus(int id, String status);

    List<CustomerService> findAllByStatusIsNotNull(Pageable pageable);
}