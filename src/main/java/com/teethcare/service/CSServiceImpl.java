package com.teethcare.service;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CSServiceImpl implements CSService {

    private final CustomerServiceRepository customerServiceRepository;
    private final RoleService roleService;

    @Override
    public List<CustomerService> findAll() {
        return customerServiceRepository.findAll();
    }

    @Override
    public CustomerService findById(int theId) {
        Optional<CustomerService> result = customerServiceRepository.findById(theId);
        if (result.isEmpty()) {
            throw new NotFoundException("CustomerService id " + theId + " not found!");
        }
        return result.get();
    }

    @Override
    public void save(CustomerService theCustomerService) {

        theCustomerService.setStatus(Status.ACTIVE.name());
        theCustomerService.setRole(roleService.getRoleByName(Role.CUSTOMER_SERVICE.name()));
        customerServiceRepository.save(theCustomerService);
    }

    @Override
    public void delete(int theId) {
        customerServiceRepository.deleteById(theId);
    }

    @Override
    public void update(CustomerService theEntity) {
        customerServiceRepository.save(theEntity);
    }

    @Override
    public List<CustomerService> findByClinicId(int theId) {
        List<CustomerService> customerServiceList = customerServiceRepository.findByClinicId(theId);

        if (customerServiceList == null || customerServiceList.size() == 0) {
            throw new NotFoundException("ID not found");
        }

        return customerServiceList;
    }

    @Override
    public List<CustomerService> findByClinicIdAndStatus(int theId, String status) {
        return customerServiceRepository.findByClinicIdAndStatus(theId, status);
    }

    @Override
    public Page<CustomerService> findAllWithPaging(Pageable pageable) {
        List<CustomerService> customerServices = customerServiceRepository.findAllByStatusIsNotNull(pageable);
        if (customerServices.size() == 0) {
            throw new NotFoundException("Empty List");
        }
        return new PageImpl<>(customerServices);
    }


}
