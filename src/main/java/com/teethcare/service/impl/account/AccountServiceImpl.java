package com.teethcare.service.impl.account;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> findAllAccounts(Pageable pageable) {
        return accountRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public Account findById(int id) {
        Account account = accountRepository.getById(id);
        return account;
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void delete(int id) {
        Account account = findById(id);
        account.setStatus(Status.Account.INACTIVE.name());
        accountRepository.save(account);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }


    @Override
    public Account getActiveAccountByUsername(String username) {
        return accountRepository.findAccountByUsernameAndStatus(username, Status.Account.INACTIVE.name());
    }

    @Override
    public List<Account> findByRoleId(int id) {
        return accountRepository.findByRoleId(id);
    }

    @Override
    public boolean isDuplicated(String username) {
        return accountRepository.getAccountByUsernameAndStatusIsNot(username, Status.Account.INACTIVE.name()) != null;
    }

    @Override
    public List<Account> searchAccountsByFullName(String search, Pageable pageable) {
        return accountRepository.searchAccountsByFullName(search, pageable);
    }
}
