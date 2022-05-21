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
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account delete(Integer id) {
        Optional<Account> accountData = accountRepository.findById(id);
        if (accountData.isPresent()) {
            Account account = accountData.get();
            account.setStatus(Status.INACTIVE.name());
            return account;
        }
        return null;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account getActiveAccountByUsername(String username) {
        return accountRepository.getAccountByUsernameAndStatusIsNot(username, Status.INACTIVE.name());
    }

    @Override
    public boolean isDuplicated(String username, String status) {
        return accountRepository.getAccountByUsernameAndStatusIsNot(username, status) != null;
    }
}
