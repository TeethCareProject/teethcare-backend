package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    Clinic getClinicByManager(Account manager);
    Clinic findClinicByCustomerServicesId(Integer csId);
    List<Clinic> findAllByStatusIsNotNull(Pageable pageable);

    Clinic findClinicById (int clinicId);

}