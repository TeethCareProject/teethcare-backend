package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CSServiceImpl implements CSService {

    private final CustomerServiceRepository customerServiceRepository;


    @Override
    public List<CustomerService> findAll() {
        return customerServiceRepository.findAll();
    }

    @Override
    public CustomerService findById(int theId) {
        Optional<CustomerService> result = customerServiceRepository.findById(theId);

        CustomerService theCustomerService = null;

        if (result.isPresent()) {
            theCustomerService = result.get();
        }

        return theCustomerService;
    }

    @Override
    public void save(CustomerService theCustomerService) {
        customerServiceRepository.save(theCustomerService);
    }

    @Override
    public void delete(int theId) {
        customerServiceRepository.deleteById(theId);
    }

    @Override
    public List<CustomerService> findByClinicId(int theId) {
        List<CustomerService> customerServiceList = customerServiceRepository.findByClinicId(theId);

        if (customerServiceList == null || customerServiceList.size() == 0) {
            throw new NotFoundException();
        }

        return customerServiceList;
    }

    @Override
    public List<CustomerService> findByClinicIdAndStatus(int theId, String status) {
        List<CustomerService> customerServiceList = customerServiceRepository.findByClinicIdAndStatus(theId, status);
        return customerServiceList;
    }

    @Override
    public CustomerService findActiveCS(int id) {
        return customerServiceRepository.findCustomerServiceByIdAndStatus(id, Status.ACTIVE.name());
    }
}
