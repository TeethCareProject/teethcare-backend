package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClinicService extends CRUDService<Clinic> {


    List<Clinic> findAll(Pageable pageable);

    Page<Clinic> findAllWithFilter(ClinicFilterRequest clinicFilterRequest,
                                   Pageable pageable);

    Clinic getClinicByManager(Manager manager);

    Clinic findById(int theId);

    void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location);

    Clinic updateProfile (ClinicRequest clinicRequest, String username);
    Clinic updateImage (MultipartFile image, String username);

}
