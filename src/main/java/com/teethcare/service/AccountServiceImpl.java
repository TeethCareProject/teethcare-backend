package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override
    public List<Account> findAll() {
        return null;
    }

    @Override
    public Account findById(int theId) {
        return null;
    }

    @Override
    public void save(Account theEntity) {

    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public List<Account> findByRoleId(int id) {
        return accountRepository.findByRoleId(id);
    }
}
