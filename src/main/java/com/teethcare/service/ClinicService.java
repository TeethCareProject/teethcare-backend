package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClinicService extends CRUDService<Clinic> {

    List<Clinic> findAllByStatus(String status, Pageable pageable);

    List<Clinic> findAll(Pageable pageable);

    Clinic getClinicByManager(Manager manager);

    Clinic findById(int theId);

    List<Clinic> searchAllActiveByName(String search, Pageable pageable);

    void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location);

}
