package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSServiceImpl implements CRUDService<CustomerService> {

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
    public Optional<CustomerService> findById(Integer id) {
        return customerServiceRepository.findById(id);
    }

    @Override
    public CustomerService save(CustomerService customerService) {
        return customerServiceRepository.save(customerService);
    }

    @Override
    public CustomerService delete(Integer id) {
        Optional<CustomerService> CSData = customerServiceRepository.findById(id);
        if (CSData.isPresent()) {
            CustomerService customerService = CSData.get();
            customerService.setStatus(Status.INACTIVE.name());
            return customerServiceRepository.save(customerService);
        }
        return null;
    }
}
