package com.teethcare.service;

import com.teethcare.model.entity.Customer;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerRepository;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSServiceImp implements CRUDService<CustomerService> {
    @Autowired
    CustomerServiceRepository customerServiceRepository;

    @Override
    public List<CustomerService> findAll() {
        return customerServiceRepository.findAll();
    }

    @Override
    public Optional<CustomerService> findById(Integer id) {
        return customerServiceRepository.findById(id);
    }

    @Override
    public ResponseEntity save(CustomerService customerService) {
        return new ResponseEntity<>(customerServiceRepository.save(customerService), HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(Integer id) {
        Optional<CustomerService> CSData = customerServiceRepository.findById(id);
        if (CSData.isPresent()) {
            CustomerService customerService = CSData.get();
            customerService.setStatus(false);
            return new ResponseEntity<>(customerServiceRepository.save(customerService), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
