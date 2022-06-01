package com.teethcare.service;

import com.teethcare.model.entity.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CSService extends CRUDService<CustomerService> {
    List<CustomerService> findByClinicId(int id);

    List<CustomerService> findByClinicIdAndStatus(int id, String status);

    Page<CustomerService> findAllWithPaging(Pageable pageable);
}
