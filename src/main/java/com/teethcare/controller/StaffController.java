package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Role;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.dto.StaffCreatingPasswordDTO;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.request.StaffPasswordRequest;
import com.teethcare.model.request.StaffRegisterRequest;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.CSService;
import com.teethcare.service.DentistService;
import com.teethcare.service.EmailService;
import com.teethcare.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Staff.STAFF_ENDPOINT)
@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).MANAGER)")
public class StaffController {

    private final DentistService dentistService;
    private final CSService csService;
    private final EmailService emailService;
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody StaffRegisterRequest staffRegisterRequest,
                                      @RequestHeader(AUTHORIZATION) String token) throws MessagingException {
        String password = StringUtils.generateRandom(Constant.PASSWORD.DEFAULT_LENGTH);
        staffRegisterRequest.setPassword(password);
        if (staffRegisterRequest.getRole().trim().equals(Role.DENTIST.name())) {
            Dentist dentist = dentistService.addNew(staffRegisterRequest, token.substring("Bearer ".length()));
            DentistResponse dentistResponse = accountMapper.mapDentistToDentistResponse(dentist);
            emailService.sendStaffCreatingPasswordEmail(new StaffCreatingPasswordDTO(staffRegisterRequest.getEmail(),staffRegisterRequest.getUsername(),staffRegisterRequest.getPassword(), "http://www.example.com/"));
            return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
        } else {
            CustomerService customerService = csService.addNew(staffRegisterRequest, token);
            CustomerServiceResponse customerServiceResponse = accountMapper.mapCustomerServiceToCustomerServiceResponse(customerService);
            emailService.sendStaffCreatingPasswordEmail(new StaffCreatingPasswordDTO(staffRegisterRequest.getEmail(),staffRegisterRequest.getUsername(),staffRegisterRequest.getPassword(), "http://www.example.com/"));
            return new ResponseEntity<>(customerServiceResponse, HttpStatus.OK);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Message> setPassword(@PathVariable("id") int id,
                                               @Valid StaffPasswordRequest staffPasswordRequest) {
        accountService.setStaffPassword(id, staffPasswordRequest);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
