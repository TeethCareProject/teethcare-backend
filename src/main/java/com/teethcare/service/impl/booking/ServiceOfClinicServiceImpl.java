package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.model.request.ServiceRequest;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.CSService;
import com.teethcare.service.FileService;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ServiceOfClinicServiceImpl implements ServiceOfClinicService {
    private final ServiceRepository serviceRepository;
    private final CSService csService;
    private final AccountService accountService;
    private final ServiceOfClinicMapper serviceOfClinicMapper;
    private final FileService fileService;

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
        return PaginationAndSortFactory.convertToPage(serviceOfClinics, pageable);
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
        return serviceOfClinics;
    }

    @Override
    public void add(ServiceRequest serviceRequest, String username) {
        CustomerService customerService = (CustomerService) accountService.getAccountByUsername(username);
        ServiceOfClinic service = serviceOfClinicMapper.mapServiceRequestToServiceOfClinic(serviceRequest);
        Clinic clinic = customerService.getClinic();
        service.setClinic(clinic);
        save(service);
    }

    @Override
    public void updateInfo(ServiceRequest serviceRequest) {
        ServiceOfClinic service = serviceRepository.findServiceOfClinicById(serviceRequest.getId());
        serviceOfClinicMapper.updateServiceOfClinicFromServiceRequest(serviceRequest, service);
        save(service);
    }

    @Override
    public void updateImage(int serviceId, MultipartFile image, String username) {
        CustomerService customerService = (CustomerService) accountService.getAccountByUsername(username);
        Clinic clinic = customerService.getClinic();
        ServiceOfClinic service = serviceRepository.findServiceOfClinicByIdAndClinic(serviceId, clinic);
        service.setImageUrl(fileService.uploadFile(image));
        update(service);
    }


    @Override
    public void save(ServiceOfClinic theEntity) {

    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public void update(ServiceOfClinic theEntity) {
        serviceRepository.save(theEntity);
    }
}
