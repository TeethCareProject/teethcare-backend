package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CSService;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.CustomerService.CUSTOMER_SERVICE_ENDPOINT)
public class CustomerServiceController {
    private final CSService CSService;
    private final AccountMapper accountMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerServiceResponse> getById(@PathVariable String id) {
        int theID = ConvertUtils.covertID(id);

        CustomerService customerService = CSService.findById(theID);

        if (customerService == null) {
            throw new NotFoundException("Customer service id " + id + "not found");
        }

        CustomerServiceResponse customerServiceResponse = accountMapper.mapCustomerServiceToCustomerServiceResponse(customerService);
        return new ResponseEntity<>(customerServiceResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);

        CustomerService customerService = CSService.findById(theID);

        if (customerService == null) {
            throw new NotFoundException("Customer service id " + id + "not found");
        }

        customerService.setId(theID);
        customerService.setStatus(Status.Account.INACTIVE.name());

        CSService.save(customerService);

        MessageResponse messageResponse = new MessageResponse(Message.SUCCESS_FUNCTION.name());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
