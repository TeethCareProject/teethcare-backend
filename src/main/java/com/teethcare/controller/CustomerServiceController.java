package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.request.CSRegisterRequest;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.CSService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.ManagerService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.CustomerService.CUSTOMER_SERVICE_ENDPOINT)
public class CustomerServiceController {
    private final CSService CSService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final ManagerService managerService;
    private final ClinicService clinicService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerServiceResponse> getById(@PathVariable int id) {
        CustomerService customerService = CSService.findById(id);

        if (customerService == null) {
            throw new NotFoundException("Customer service id " + id + "not found");
        }

        CustomerServiceResponse customerServiceResponse = accountMapper.mapCustomerServiceToCustomerServiceResponse(customerService);
        return new ResponseEntity<>(customerServiceResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerServiceResponse>> getAll(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        Page<CustomerService> customerServices = CSService.findAllWithPaging(pageable);
        Page<CustomerServiceResponse> customerServiceResponses = customerServices.map(accountMapper::mapCustomerServiceToCustomerServiceResponse);
        return new ResponseEntity<>(customerServiceResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerServiceResponse> add(@Valid @RequestBody CSRegisterRequest csRegisterRequest,
                                                       HttpServletRequest request) {
        boolean isDuplicated = accountService.isDuplicated(csRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (csRegisterRequest.getPassword().equals(csRegisterRequest.getConfirmPassword())) {
                String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
                String username = jwtTokenUtil.getUsernameFromJwt(token);
                Account account = accountService.getAccountByUsername(token);
                Clinic clinic = clinicService.getClinicByManager(managerService.findById(account.getId()));

                CustomerService customerService = accountMapper.mapCSRegisterRequestToCustomerService(csRegisterRequest);
                customerService.setClinic(clinic);
                CSService.save(customerService);
                CustomerServiceResponse customerServiceResponse = accountMapper.mapCustomerServiceToCustomerServiceResponse(customerService);
                return new ResponseEntity<>(customerServiceResponse, HttpStatus.OK);
            } else {
                throw new BadRequestException("confirm Password is not match with password");
            }
        } else {
            throw new BadRequestException("User existed!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable("id") int id) {
        CustomerService customerService = CSService.findById(id);

        if (customerService == null) {
            throw new NotFoundException("Customer service id " + id + "not found");
        }

        customerService.setId(id);
        customerService.setStatus(Status.Account.INACTIVE.name());

        CSService.save(customerService);

        MessageResponse messageResponse = new MessageResponse(Message.SUCCESS_FUNCTION.name());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
