package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImp implements CRUDService<Account>, AccountService {
    @Autowired
    AccountRepository accountRepository;

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
    public ResponseEntity delete(Integer id) {
        Optional<Account> accountData = accountRepository.findById(id);
        if (accountData.isPresent()) {
            Account account = accountData.get();
            account.setStatus(false);
            return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }
}
