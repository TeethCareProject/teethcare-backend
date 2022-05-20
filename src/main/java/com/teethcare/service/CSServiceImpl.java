package com.teethcare.service;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSServiceImpl implements CRUDService<CustomerService>{

    private CustomerServiceRepository customerServiceRepository;

    @Autowired
    public CSServiceImpl(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
    }

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
    public void deleteById(int theId) {
        customerServiceRepository.deleteById(theId);
    }
}
