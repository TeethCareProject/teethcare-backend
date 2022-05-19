package com.teethcare.service;

import com.teethcare.model.entity.Clinic;

public interface ClinicService {
    Iterable<Clinic> findAll();
}
