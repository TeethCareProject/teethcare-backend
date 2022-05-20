package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import org.springframework.stereotype.Service;

import java.util.Collection;

public interface ClinicService extends CRUDService<Clinic> {
    Collection<Clinic> findAllActive();
}
