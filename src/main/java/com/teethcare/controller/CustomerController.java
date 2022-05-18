package com.teethcare.controller;

import com.teethcare.model.entity.Customer;
import com.teethcare.model.entity.Customer;
import com.teethcare.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable("id") String id) {
        return customerRepository.findById(id);
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Customer> delCustomer(@PathVariable("id") String id) {
        Optional<Customer> CustomerData = customerRepository.findById(id);
        if (CustomerData.isPresent()) {
            Customer customer = CustomerData.get();
            customer.getAccount().setStatus(false);
            return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
