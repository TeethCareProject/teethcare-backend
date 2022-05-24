package com.teethcare.service;

import com.teethcare.model.entity.ServiceOfClinic;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceOfClinicService extends CRUDService<ServiceOfClinic>{
    List<ServiceOfClinic> findByClinicIdAndStatus(int theClinicId, String status, Pageable pageable);
}
