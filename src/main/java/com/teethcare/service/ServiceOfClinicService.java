package com.teethcare.service;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceOfClinicService extends CRUDService<ServiceOfClinic> {
    Page<ServiceOfClinic> findByClinicIdAndStatus(int theClinicId, String status, Pageable pageable);
    ServiceOfClinic findById(int theId);
    Page<ServiceOfClinic> findAllWithFilter(ServiceFilterRequest serviceFilterRequest, Pageable pageable);
}
