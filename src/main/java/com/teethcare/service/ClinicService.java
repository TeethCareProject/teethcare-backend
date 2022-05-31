package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ClinicFilterRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClinicService extends CRUDService<Clinic> {


    List<Clinic> findAll(Pageable pageable);

    List<Clinic> findAllWithFilter(ClinicFilterRequest clinicFilterRequest,
                                   String status,
                                   Pageable pageable);

    Clinic getClinicByManager(Manager manager);

    Clinic findById(int theId);


    void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location);

}
