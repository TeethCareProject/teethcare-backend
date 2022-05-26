package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.exception.IdInvalidException;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CSService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.CustomerService.CUSTOMER_SERVICE_ENDPOINT)
public class CustomerServiceController {

    private final CSService CSService;
    private final AccountMapper accountMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerServiceResponse> getCSById(@PathVariable String id) {
        int theID = 0;

        if (!NumberUtils.isCreatable(id)) {
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);

        CustomerService customerService = CSService.findById(theID);

        if (customerService == null) {
            throw new IdNotFoundException("Customer service id " + id + "not found");
        }

        CustomerServiceResponse customerServiceResponse = accountMapper.mapCustomerServiceToCustomerServiceResponse(customerService);
        return new ResponseEntity<>(customerServiceResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateAccountStatus(@PathVariable("id") String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);

        CustomerService customerService = CSService.findById(theID);

        if (customerService == null) {
            throw new IdNotFoundException("Customer service id " + id + "not found");
        }

        customerService.setId(theID);
        customerService.setStatus(Status.INACTIVE.name());

        CSService.save(customerService);

        MessageResponse messageResponse = new MessageResponse(Message.SUCCESS_FUNCTION.name());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
