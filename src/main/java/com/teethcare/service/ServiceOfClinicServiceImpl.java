package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceOfClinicServiceImpl implements ServiceOfClinicService{
    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceOfClinicServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceOfClinic> findAll() {
        return null;
    }

    @Override
    public ServiceOfClinic findById(int theId) {
        return null;
    }

    @Override
    public void save(ServiceOfClinic theEntity) {

    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public List<ServiceOfClinic> findByClinicIdAndStatus(int theClinicId, String status, Pageable pageable) {
        List<ServiceOfClinic> serviceOfClinicList =
                serviceRepository.findByClinicIdAndStatus(theClinicId, Status.ACTIVE.name(), pageable);

        if (serviceOfClinicList == null || serviceOfClinicList.size() == 0) {
            throw new NotFoundException();
        }
        return serviceOfClinicList;
    }
}
