package com.teethcare.controller;

import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
//@EnableSwagger2
@RequestMapping("/api/customer-services")
public class CustomerServiceController {

    private CRUDService<CustomerService> CSService;

    @Autowired
    public CustomerServiceController (@Qualifier("CSServiceImpl") CRUDService<CustomerService> CSService) {
        this.CSService = CSService;
    }

    @GetMapping()
    public List<CustomerService> findAll() {
        return CSService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerService getCSById(@PathVariable int id) {

        CustomerService customerService = CSService.findById(id);

        if (customerService == null) {
            throw new NotFoundException();
        }

        return customerService;
    }

    @PutMapping
    public CustomerService updateCS(@RequestBody CustomerService customerService) {

        CSService.save(customerService);

        return customerService;
    }

    @DeleteMapping("/{id}")
    public CustomerService updateAccountStatus(@PathVariable("id") int id) {
        if (id < 1) {
            throw new NotFoundException();
        }

        CustomerService customerService = CSService.findById(id);

        if (customerService == null) {
            throw new NotFoundException();
        }

        customerService.setId(id);
        customerService.setStatus(false);

        CSService.save(customerService);

        return customerService;
    }
}
