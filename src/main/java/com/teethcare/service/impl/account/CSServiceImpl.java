package com.teethcare.service.impl.account;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.request.CSRegisterRequest;
import com.teethcare.repository.CustomerServiceRepository;
import com.teethcare.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CSServiceImpl implements CSService {

    private final CustomerServiceRepository customerServiceRepository;
    private final RoleService roleService;
    private final AccountService accountService;
    private final ClinicService clinicService;
    private final ManagerService managerService;
    private final AccountMapper accountMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerService> findAll() {
        return customerServiceRepository.findAll();
    }

    @Override
    public CustomerService findById(int theId) {
        Optional<CustomerService> result = customerServiceRepository.findById(theId);
        if (result.isEmpty()) {
            throw new NotFoundException("CustomerService id " + theId + " not found!");
        }
        return result.get();
    }

    @Override
    public void save(CustomerService theCustomerService) {
        theCustomerService.setStatus(Status.Account.ACTIVE.name());
        theCustomerService.setRole(roleService.getRoleByName(Role.CUSTOMER_SERVICE.name()));
        theCustomerService.setPassword(passwordEncoder.encode(theCustomerService.getPassword()));
        customerServiceRepository.save(theCustomerService);
    }

    @Override
    public void delete(int theId) {
        customerServiceRepository.deleteById(theId);
    }

    @Override
    public void update(CustomerService theEntity) {
        customerServiceRepository.save(theEntity);
    }

    @Override
    public List<CustomerService> findByClinicId(int theId) {
        List<CustomerService> customerServiceList = customerServiceRepository.findByClinicId(theId);

        if (customerServiceList == null || customerServiceList.size() == 0) {
            throw new NotFoundException("ID not found");
        }

        return customerServiceList;
    }

    @Override
    public List<CustomerService> findByClinicIdAndStatus(int theId, String status) {
        return customerServiceRepository.findByClinicIdAndStatus(theId, status);
    }

    @Override
    public Page<CustomerService> findAllWithPaging(Pageable pageable) {
        List<CustomerService> customerServices = customerServiceRepository.findAllByStatusIsNotNull(pageable);
        return new PageImpl<>(customerServices);
    }

    @Override
    public CustomerService addNew(CSRegisterRequest csRegisterRequest, String token) {
        csRegisterRequest.trim();
        boolean isDuplicated = accountService.isDuplicated(csRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (csRegisterRequest.getPassword().equals(csRegisterRequest.getConfirmPassword())) {
                token = token.substring("Bearer ".length());
                String username = jwtTokenUtil.getUsernameFromJwt(token);
                Account account = accountService.getAccountByUsername(username);
                Clinic clinic = clinicService.getClinicByManager(managerService.findById(account.getId()));

                CustomerService customerService = accountMapper.mapCSRegisterRequestToCustomerService(csRegisterRequest);
                customerService.setClinic(clinic);
                this.save(customerService);
                return customerService;
            } else {
                throw new BadRequestException("confirm Password is not match with password");
            }
        } else {
            throw new BadRequestException("User existed!");
        }
    }
}
