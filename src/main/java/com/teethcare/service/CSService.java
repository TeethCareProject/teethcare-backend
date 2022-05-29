package com.teethcare.service;

import com.teethcare.model.entity.CustomerService;

import java.util.List;

public interface CSService extends CRUDService<CustomerService> {
    List<CustomerService> findByClinicId(int id);

    List<CustomerService> findByClinicIdAndStatus(int id, String status);

    CustomerService findActiveCS(int id);
}
