package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.model.entity.Account;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Account.ACCOUNT_ENDPOINT)
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper mapper;


    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            accountResponses.add(new AccountResponse(
                account.getId(), account.getUsername(), account.getRole().getName(), account.getFirstName(),
                    account.getLastName(), account.getAvatarImage(), account.getDateOfBirth(), account.getEmail(),
                    account.getPhone(), account.getGender(), account.getStatus()
            ));
        }
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }
}
