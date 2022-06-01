package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.service.ClinicService;
import com.teethcare.service.ServiceOfClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public Page<Clinic> findAllWithFilter(ClinicFilterRequest filter, Pageable pageable) {
        List<Clinic> list = new ArrayList<>();
        if (filter != null) {
            if (filter.getName() != null) {
                list = clinicRepository.findAllByNameContainingIgnoreCase(filter.getName().replaceAll("\\s\\s+", " ").trim(), pageable);
            } else {
                list = clinicRepository.findAllByStatusIsNotNull(pageable);
            }
            if (filter.getStatus() != null) {
                Predicate<Clinic> byStatus = (clinic) -> clinic.getStatus().equals(filter.getStatus());
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
            if (filter.getId() != null) {
                Predicate<Clinic> byClinicId = (clinic) -> clinic.getId().toString().toUpperCase()
                        .contains(filter.getId().trim().toUpperCase());
                list = list.stream().filter(byClinicId).collect(Collectors.toList());
            }
            if (filter.getServiceId() != null) {
                List<Clinic> tmpList = new ArrayList<>();
                for (Clinic clinic : list) {
                    boolean check = false;
                    List<ServiceOfClinic> serviceOfClinicList = clinic.getServiceOfClinic();
                    for (ServiceOfClinic service : serviceOfClinicList) {
                        if (service.getId().toString().toUpperCase()
                                .contains(filter.getServiceId().trim().toUpperCase())) {
                            check = true;
                            break;
                        }
                    }
                    if (check) {
                        tmpList.add(clinic);
                    }
                }
                list = tmpList;
            }
            if (filter.getServiceName() != null) {
                List<Clinic> tmpList = new ArrayList<>();
                for (Clinic clinic : list) {
                    boolean check = false;
                    List<ServiceOfClinic> serviceOfClinicList = clinic.getServiceOfClinic();
                    for (ServiceOfClinic service : serviceOfClinicList) {
                        if (service.getName().toUpperCase().contains(filter.getServiceName()
                                .replaceAll("\\s\\s+", " ").trim().toUpperCase())) {
                            check = true;
                            break;
                        }
                    }
                    if (check) {
                        tmpList.add(clinic);
                    }
                }
                list = tmpList;
            }
        }
        return new PageImpl<>(list);
    }

    public List<Clinic> findAllActive(Pageable pageable) {
        return clinicRepository.findAllByStatus(Status.Clinic.ACTIVE.name(), pageable);
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
        clinic.setStatus(Status.Clinic.PENDING.name());
        clinicRepository.save(clinic);
    }

    @Override
    @Transactional
    public void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location) {
        clinic.setManager(manager);
        clinic.setLocation(location);
        clinic.setStatus(Status.Clinic.PENDING.name());
        clinicRepository.save(clinic);
    }

    @Override
    public void delete(int id) {
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(Status.Clinic.INACTIVE.name());
            clinicRepository.save(clinic);
        } else {
            throw new NotFoundException("ID not found");
        }
    }

    @Override
    public void update(Clinic theEntity) {
        clinicRepository.save(theEntity);
    }

}
