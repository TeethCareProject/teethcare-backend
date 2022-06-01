package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.CSService;
import com.teethcare.service.ServiceOfClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfClinicServiceImpl implements ServiceOfClinicService {
    private final ServiceRepository serviceRepository;
    private final CSService csService;

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
    public ServiceOfClinic findById(int theId, Account account) {
        ServiceOfClinic serviceOfClinic = null;
        if (account == null || !account.getRole().getName().equals(Role.CUSTOMER_SERVICE.name())) {
            serviceOfClinic = serviceRepository.findByIdAndStatus(theId, Status.ACTIVE.name());
        } else {
            serviceOfClinic = serviceRepository.getById(theId);
            if (serviceOfClinic.getClinic().getId() != csService.findById(account.getId()).getClinic().getId() &&
                    serviceOfClinic.getStatus().equals(Status.INACTIVE.name())) {
                serviceOfClinic = null;

            }
        }
        if (serviceOfClinic == null) {
            throw new EntityNotFoundException("Service was not found");
        }
        return serviceOfClinic;
    }

    @Override
    public Page<ServiceOfClinic> findAllWithFilter(ServiceFilterRequest request, Pageable pageable, Account account) {
        List<ServiceOfClinic> serviceOfClinics = null;
        if (account == null || !account.getRole().getName().equals(Role.CUSTOMER_SERVICE.name())) {
            serviceOfClinics = serviceRepository.findAllByStatus(pageable, Status.ACTIVE.name());
        } else {
            serviceOfClinics = serviceRepository.findAll();
            for (int i = 0; i < serviceOfClinics.size(); i++) {
                if (serviceOfClinics.get(i).getClinic().getId() != csService.findById(account.getId()).getClinic().getId() &&
                        serviceOfClinics.get(i).getStatus().equals(Status.INACTIVE.name())) {
                    serviceOfClinics.remove(serviceOfClinics.get(i));
                }
            }
        }
        if (request.getId() != null) {
            Predicate<ServiceOfClinic> byID = service -> service.getId().toString().contains(request.getId().toString());
            serviceOfClinics = serviceOfClinics.stream()
                    .filter(byID)
                    .collect(Collectors.toList());
        }
        if (request.getName() != null) {
            Predicate<ServiceOfClinic> byName = (service) -> service.getName().toLowerCase().contains(request.getName().toLowerCase());
            serviceOfClinics = serviceOfClinics.stream()
                    .filter(byName)
                    .collect(Collectors.toList());
        }
        if (request.getClinicID() != null) {
            Predicate<ServiceOfClinic> byClinicID = (service) -> service.getClinic().getId() == request.getClinicID();
            serviceOfClinics = serviceOfClinics.stream()
                    .filter(byClinicID)
                    .collect(Collectors.toList());
        }
        if (request.getLowerPrice() != null && request.getUpperPrice() != null) {
            Predicate<ServiceOfClinic> byRangePrice = (service) -> service.getMoney().compareTo(request.getLowerPrice()) >= 0
                    && service.getMoney().compareTo(request.getUpperPrice()) <= 0;
            serviceOfClinics = serviceOfClinics.stream()
                    .filter(byRangePrice)
                    .collect(Collectors.toList());
        }
        if (request.getLowerPrice() != null && request.getUpperPrice() == null) {
            Predicate<ServiceOfClinic> byLowerPrice = (service) -> service.getMoney().compareTo(request.getLowerPrice()) >= 0;
            serviceOfClinics = serviceOfClinics.stream()
                    .filter(byLowerPrice)
                    .collect(Collectors.toList());
        }
        if (request.getLowerPrice() == null && request.getUpperPrice() != null) {
            Predicate<ServiceOfClinic> byUpperPrice = (service) -> service.getMoney().compareTo(request.getUpperPrice()) <= 0;
            serviceOfClinics = serviceOfClinics.stream()
                    .filter(byUpperPrice)
                    .collect(Collectors.toList());
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
}
