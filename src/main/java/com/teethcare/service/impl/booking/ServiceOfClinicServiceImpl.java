package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.ServiceOfClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceOfClinicServiceImpl implements ServiceOfClinicService {
    private final ServiceRepository serviceRepository;

    @Override
    public List<ServiceOfClinic> findAll() {
        return serviceRepository.findAll();
    }


    @Override
    public ServiceOfClinic findById(int theId) {
        ServiceOfClinic service = serviceRepository.getById(theId);
        return service;
    }

    @Override
    public List<ServiceOfClinic> findAll(Pageable pageable) {
        return serviceRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public List<ServiceOfClinic> findByStatus(Pageable pageable, String status) {
        return serviceRepository.findAllByStatus(pageable, status);
    }

    @Override
    public void save(ServiceOfClinic theEntity) {
    }

    @Override
    public void delete(int theId) {
    }

    @Override
    public List<ServiceOfClinic> findByClinicIdAndStatus(int theClinicId, String status, Pageable pageable) {
        List<ServiceOfClinic> serviceOfClinicList =
                serviceRepository.findByClinicIdAndStatus(theClinicId, Status.ACTIVE.name(), pageable);

        if (serviceOfClinicList == null || serviceOfClinicList.size() == 0) {
            throw new NotFoundException("ID not found");
        }
        return serviceOfClinicList;
    }
}
