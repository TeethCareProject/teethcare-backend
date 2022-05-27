package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClinicService extends CRUDService<Clinic> {
    List<Clinic> findAllActive(Pageable pageable);

    Clinic getClinicByManager(Manager manager);
    Clinic findById(int theId);
    List<Clinic> searchAllActiveByName(String search, Pageable pageable);

}
