package com.teethcare.service;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSServiceImpl implements AccountService<CustomerService>{

    private CustomerServiceRepository customerServiceRepository;

    @Autowired
    public CSServiceImpl(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
    }

    @Override
    public List<CustomerService> findAll() {
        return null;
    }

    @Override
    public CustomerService findById(int theId) {
        return null;
    }

    @Override
    public CustomerService findById(String theId) {
        Optional<CustomerService> result = customerServiceRepository.findById(theId);

        CustomerService theCustomerService = null;

        if (result.isPresent()) {
            theCustomerService = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theCustomerService;
    }

    @Override
    public void save(CustomerService theCustomerService) {
        customerServiceRepository.save(theCustomerService);
    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public void deleteById(String theId) {
        customerServiceRepository.deleteById(theId);
    }
}
