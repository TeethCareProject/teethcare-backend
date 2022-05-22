package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImp implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsernameAndStatus(username, Status.ACTIVE.name());
    }
}
