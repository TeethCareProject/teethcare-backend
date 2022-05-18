package com.teethcare.controller;

import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@RequestMapping("/")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;
    @PostMapping("/account/")
    public Account addAccount(@Validated  @RequestBody Account account) {
        return  accountRepository.save(account);
    }

}
