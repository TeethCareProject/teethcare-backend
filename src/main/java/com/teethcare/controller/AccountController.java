package com.teethcare.controller;

import com.teethcare.model.entity.Account;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableSwagger2
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;
    private ModelMapper mapper;

    @Autowired
    public AccountController(AccountService accountService, ModelMapper mapper) {
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            accountResponses.add(new AccountResponse(account.getId(),
                    account.getUsername(),
                    account.getRole().getName(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getGender(),
                    account.getEmail(),
                    account.getPhoneNumber(),
                    account.getStatus()));
        }
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Account> getAllActiveAccounts(@PathVariable("username") String username) {
        return new ResponseEntity<>(accountService.getActiveAccountByUsername(username), HttpStatus.OK);

    }
}
