package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<Account> accountData = accountRepository.findById(id);
        Account account = accountData.get();
        account.setStatus(Status.INACTIVE.name());
        accountRepository.save(account);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }


    @Override
    public Account getActiveAccountByUsername(String username) {
        return accountRepository.findAccountByUsernameAndStatus(username, Status.ACTIVE.name());
    }

    @Override
    public boolean isDuplicated(String username) {
        return accountRepository.getAccountByUsernameAndStatusIsNot(username, Status.INACTIVE.name()) != null;
    }
}
