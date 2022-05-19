package com.teethcare.controller;

import com.teethcare.model.entity.Account;
import com.teethcare.service.AccountServiceImp;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@EnableSwagger2
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountServiceImp accountServiceImp;
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Account> getAllAccounts() {
        return accountServiceImp.findAll();
    }
}
