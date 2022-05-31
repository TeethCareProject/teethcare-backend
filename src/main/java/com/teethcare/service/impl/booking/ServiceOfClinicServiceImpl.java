package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.ServiceOfClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public Page<ServiceOfClinic> findAllWithFilter(ServiceFilterRequest request, Pageable pageable) {
        List<ServiceOfClinic> serviceOfClinics = serviceRepository.findAll();
        if (request.getName() != null) {
            serviceOfClinics = serviceRepository.findAllByNameContainingIgnoreCase(request.getName(), pageable);
        }
        if (request.getClinicID() != null) {
            Predicate<ServiceOfClinic> byClinicID = (service) -> service.getClinic().getId() == request.getClinicID();
            serviceOfClinics = serviceOfClinics.stream().filter(byClinicID).collect(Collectors.toList());
        }
        if (request.getLowerPrice() != null && request.getUpperPrice() != null) {
            Predicate<ServiceOfClinic> byRangePrice = (service) -> service.getMoney().compareTo(request.getLowerPrice()) >= 0
                    && service.getMoney().compareTo(request.getUpperPrice()) <= 0;
            serviceOfClinics = serviceOfClinics.stream().filter(byRangePrice).collect(Collectors.toList());
        }
        if (request.getLowerPrice() != null && request.getUpperPrice() == null) {
            Predicate<ServiceOfClinic> byLowerPrice = (service) -> service.getMoney().compareTo(request.getLowerPrice()) >= 0;
            serviceOfClinics = serviceOfClinics.stream().filter(byLowerPrice).collect(Collectors.toList());
        }
        if (request.getLowerPrice() == null && request.getUpperPrice() != null) {
            Predicate<ServiceOfClinic> byUpperPrice = (service) -> service.getMoney().compareTo(request.getUpperPrice()) <= 0;
            serviceOfClinics = serviceOfClinics.stream().filter(byUpperPrice).collect(Collectors.toList());
        }
        Page<ServiceOfClinic> lists = new PageImpl<>(serviceOfClinics);
        return lists;
    }


    @Override
    public void save(ServiceOfClinic theEntity) {

    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public Page<ServiceOfClinic> findByClinicIdAndStatus(int theClinicId, String status, Pageable pageable) {
        return null;
    }
}
