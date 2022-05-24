package com.teethcare.service;

import com.teethcare.config.model.entity.Clinic;
import com.teethcare.config.model.entity.Manager;

import java.util.List;

public interface ClinicService extends CRUDService<Clinic> {
    List<Clinic> findAllActive();
    Clinic getClinicByManager(Manager manager);

}
