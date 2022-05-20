package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query(
            value = "SELECT * FROM Customer c WHERE c.status <> 0",
            nativeQuery = true)
    Collection<Customer> findAllActive();
}
