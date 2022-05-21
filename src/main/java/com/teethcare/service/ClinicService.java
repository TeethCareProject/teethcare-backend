package com.teethcare.service;

import com.teethcare.model.entity.Clinic;

import java.util.Collection;

public interface ClinicService extends CRUDService<Clinic> {
    Collection<Clinic> findAllActive();
}
