package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.ServiceOfClinic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceOfClinic, Integer> {
    List<ServiceOfClinic> findByClinicIdAndStatus(int id, String status, Pageable pageable);
    List<ServiceOfClinic> findAllByStatus(Pageable pageable, String status);
    ServiceOfClinic findByIdAndStatus(int id, String status);
    ServiceOfClinic findServiceOfClinicById(int id);
    ServiceOfClinic findServiceOfClinicByIdAndClinic(int id, Clinic clinic);
}
