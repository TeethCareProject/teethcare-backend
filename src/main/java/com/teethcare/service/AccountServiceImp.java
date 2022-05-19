package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
<<<<<<< Updated upstream
public class AccountServiceImp implements AccountService{
=======
public class AccountServiceImp implements CRUDService<Account>, AccountService {
>>>>>>> Stashed changes
    @Autowired
    AccountRepository accountRepository;
    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }
}
