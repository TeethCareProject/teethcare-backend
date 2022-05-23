package com.teethcare.service;

import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSServiceImpl implements CSService{

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

        System.out.println(customerServiceList);
        if (customerServiceList == null || customerServiceList.size() == 0) {
            throw new NotFoundException();
        }

        return customerServiceList;
    }
}
