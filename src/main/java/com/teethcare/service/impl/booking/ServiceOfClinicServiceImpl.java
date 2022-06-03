package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
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
            serviceOfClinic = serviceRepository.findByIdAndStatus(theId, Status.Service.ACTIVE.name());
        } else {
            serviceOfClinic = serviceRepository.getById(theId);
            if (serviceOfClinic.getClinic().getId() != csService.findById(account.getId()).getClinic().getId() &&
                    serviceOfClinic.getStatus().equals(Status.Service.INACTIVE.name())) {
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
        List<ServiceOfClinic> serviceOfClinics = findAllByRole(account, pageable);
        serviceOfClinics = serviceOfClinics.stream()
                .filter(request.predicates().stream().reduce(serviceOfClinic -> true, Predicate::and))
                .collect(Collectors.toList());
        return new PageImpl<>(serviceOfClinics);
    }

    @Override
    public List<ServiceOfClinic> findAllByRole(Account account, Pageable pageable) {
        List<ServiceOfClinic> serviceOfClinics = null;
        if (account == null || !account.getRole().getName().equals(Role.CUSTOMER_SERVICE.name())) {
            serviceOfClinics = serviceRepository.findAllByStatus(pageable, Status.Service.ACTIVE.name());
        } else {
            serviceOfClinics = serviceRepository.findAll();
            Predicate<ServiceOfClinic> condition = serviceOfClinic -> serviceOfClinic.getClinic().getId() != csService.findById(account.getId()).getClinic().getId() &&
                    serviceOfClinic.getStatus().equals(Status.Service.INACTIVE.name());
            serviceOfClinics.removeIf(condition);
        }
        return  serviceOfClinics;
    }


    @Override
    public void save(ServiceOfClinic theEntity) {

    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public void update(ServiceOfClinic theEntity) {

    }
}
