package com.teethcare.controller;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.service.CSServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CSServiceImp csServiceImp;

    @GetMapping
    public List<CustomerService> getAllCustomerServices() {
        return csServiceImp.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CustomerService> getCustomerService(@PathVariable("id") Integer id) {
        return csServiceImp.findById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerService> addCustomerService(@RequestBody CustomerService customerService) {
        return csServiceImp.save(customerService);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<CustomerService> delCustomerService(@PathVariable("id") Integer id) {
        return csServiceImp.delete(id);
    }
}
