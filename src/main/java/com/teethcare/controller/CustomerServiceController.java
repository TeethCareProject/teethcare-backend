package com.teethcare.controller;

import com.teethcare.common.Message;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.config.mapper.MapStructMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
//@EnableSwagger2
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/customer-services")
public class CustomerServiceController {

    private CRUDService<CustomerService> CSService;
    private AccountMapper accountMapper;

    @Autowired
    public CustomerServiceController (@Qualifier("CSServiceImpl") CRUDService<CustomerService> CSService,
                                      AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        this.CSService = CSService;
    }

    @GetMapping()
    public List<CustomerService> findAll() {
        return CSService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerServiceResponse> getCSById(@PathVariable int id) {

        CustomerService customerService = CSService.findById(id);

        if (customerService == null) {
            throw new NotFoundException();
        }

        CustomerServiceResponse customerServiceResponse = new CustomerServiceResponse();
        customerServiceResponse = accountMapper.mapCustomerServiceToCustomerServiceResponse(customerService);

        return new ResponseEntity<>(customerServiceResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateAccountStatus(@PathVariable("id") int id) {
        if (id < 1) {
            throw new NotFoundException();
        }

        CustomerService customerService = CSService.findById(id);

        if (customerService == null) {
            throw new NotFoundException();
        }

        customerService.setId(id);
        customerService.setStatus("INACTIVE");

        CSService.save(customerService);

        return new ResponseEntity<>(Message.SUCCESS_FUNCTION.name(), HttpStatus.OK);
    }
}
