package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Account.ACCOUNT_ENDPOINT)
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;


    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        List<AccountResponse> accountResponses = accountMapper.mapAccountListToAccountResponseList(accounts);
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity getAccountById(@PathVariable("id") int id) {
        Account account = accountService.findById(id);
        if (account != null) {
            AccountResponse accountResponse = accountMapper.mapAccountToAccountResponse(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.OK);
        } else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
