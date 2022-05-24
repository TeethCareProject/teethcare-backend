package com.teethcare.repository;

import com.teethcare.config.model.entity.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Integer> {
}