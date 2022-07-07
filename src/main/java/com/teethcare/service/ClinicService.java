package com.teethcare.service;

import com.teethcare.model.dto.ClinicDTO;
import com.teethcare.model.dto.LocationDTO;
import com.teethcare.model.dto.ManagerDTO;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.response.ClinicResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

public interface ClinicService extends CRUDService<Clinic> {


    List<Clinic> findAll(Pageable pageable);
    Page<ClinicResponse> findNear(Double longitude, Double latitude, String username, Pageable pageable);

    Page<Clinic> findAllWithFilter(ClinicFilterRequest clinicFilterRequest,
                                   Pageable pageable);

    Clinic getClinicByManager(Manager manager);
    Clinic findClinicByCustomerServiceId(int csId);
    Clinic findById(int theId);
    Clinic create(ClinicDTO clinicDTO, LocationDTO locationDTO, Manager manager);

    void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location);

    Clinic updateProfile (ClinicRequest clinicRequest, String username);
    Clinic updateImage (MultipartFile image, String username);
    Clinic approve(Clinic clinic) throws MessagingException;
    Clinic reject(Clinic clinic) throws MessagingException;

}
