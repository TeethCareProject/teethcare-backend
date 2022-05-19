package com.teethcare.controller;

import com.teethcare.model.entity.Customer;
import com.teethcare.service.CustomerServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CustomerServiceImp customerServiceImp;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerServiceImp.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable("id") Integer id) {
        return customerServiceImp.findById(id);
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerServiceImp.save(customer);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Customer> delCustomer(@PathVariable("id") Integer id) {
        return customerServiceImp.delete(id);
    }
}
