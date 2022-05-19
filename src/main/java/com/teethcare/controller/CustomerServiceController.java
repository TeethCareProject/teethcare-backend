package com.teethcare.controller;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/customer-services")
public class CustomerServiceController {
    @Autowired
    private CustomerServiceRepository customerServiceRepository;

    @GetMapping
    public List<CustomerService> getAllCustomerServices() {
        return customerServiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CustomerService> getCustomerService(@PathVariable("id") String id) {
        return customerServiceRepository.findById(id);
    }

    @PostMapping
    public CustomerService addCustomerService(@RequestBody CustomerService customerService) {
        return customerServiceRepository.save(customerService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerService> delCustomerService(@PathVariable("id") String id) {
        Optional<CustomerService> customerServiceData = customerServiceRepository.findById(id);
        if (customerServiceData.isPresent()) {
            CustomerService customerService = customerServiceData.get();
            customerService.setStatus(false);
            return new ResponseEntity<>(customerServiceRepository.save(customerService), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
