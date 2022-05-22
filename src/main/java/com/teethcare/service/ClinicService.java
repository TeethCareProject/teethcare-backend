package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;

import java.util.Collection;

public interface ClinicService extends CRUDService<Clinic> {
    Collection<Clinic> findAllActive();
    Clinic getClinicByManager(Manager manager);

}
