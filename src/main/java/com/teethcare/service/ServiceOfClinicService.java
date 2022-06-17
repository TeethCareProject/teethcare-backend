package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.model.request.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceOfClinicService extends CRUDService<ServiceOfClinic> {
    ServiceOfClinic findById(int theId, Account account);

    Page<ServiceOfClinic> findAllWithFilter(ServiceFilterRequest serviceFilterRequest, Pageable pageable, Account account);

    List<ServiceOfClinic> findAllByRole(Account account, Pageable pageable);
    void add(ServiceRequest serviceRequest, String username);

    void updateInfo(ServiceRequest serviceRequest);

}
