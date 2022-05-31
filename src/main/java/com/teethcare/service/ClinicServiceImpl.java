package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;

    private final ServiceOfClinicService serviceOfClinicService;

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }


    @Override
    public List<Clinic> findAll(Pageable pageable) {
        return clinicRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public List<Clinic> findAllWithFilter(ClinicFilterRequest filter, String status, Pageable pageable) {
        List<Clinic> list = null;
        if (filter != null) {
            if (filter.getName() != null) {
                list = clinicRepository.findAllByNameContainingIgnoreCaseAndStatus(filter.getName(), Status.ACTIVE.name(), pageable);
            } else {
                list = clinicRepository.findAllByStatusIsNotNull(pageable);
            }
            if (status != null) {
                Predicate<Clinic> byStatus = (clinic) -> clinic.getStatus().equals(status);
                list = list.stream().filter(byStatus).collect(Collectors.toList());
            }
            if (filter.getProvinceId() != null) {
                Predicate<Clinic> byProvinceId = (clinic) -> clinic.getLocation().getWard().getDistrict().getProvince().getId() == filter.getProvinceId();
                list = list.stream().filter(byProvinceId).collect(Collectors.toList());
            }
            if (filter.getDistrictId() != null) {
                Predicate<Clinic> byDistrictId = (clinic) -> clinic.getLocation().getWard().getDistrict().getId() == filter.getDistrictId();
                list = list.stream().filter(byDistrictId).collect(Collectors.toList());
            }
            if (filter.getWardId() != null) {
                Predicate<Clinic> byWardId = (clinic) -> clinic.getLocation().getWard().getId() == filter.getWardId();
                list = list.stream().filter(byWardId).collect(Collectors.toList());
            }
            if (filter.getServiceList() != null) {
                for (int clinicId : filter.getServiceList()) {
                    ServiceOfClinic serviceOfClinic = serviceOfClinicService.findById(clinicId);
                    Predicate<Clinic> byServiceId = (clinic) -> clinic.getServiceOfClinic().contains(serviceOfClinic);
                    list = list.stream().filter(byServiceId).collect(Collectors.toList());
                }
            }
        }

        return list;
    }


    public Clinic getClinicByManager(Manager manager) {
        return clinicRepository.getClinicByManager(manager);
    }

    @Override
    public Clinic findById(int theId) {
        Optional<Clinic> result = clinicRepository.findById(theId);
        if (result.isEmpty()) {
            throw new NotFoundException("Clinic id " + theId + " not found!");
        }
        return result.get();
    }



    @Override
    public void save(Clinic clinic) {
        clinic.setStatus(Status.PENDING.name());
        clinicRepository.save(clinic);
    }

    @Override
    @Transactional
    public void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location) {
        clinic.setManager(manager);
        clinic.setLocation(location);
        clinic.setStatus(Status.PENDING.name());
        clinicRepository.save(clinic);
    }

    @Override
    public void delete(int id) {
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        Clinic clinic = clinicData.get();
        clinic.setStatus(Status.INACTIVE.name());
        clinicRepository.save(clinic);
    }

}
