package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.AccountFilterRequest;
import com.teethcare.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


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
    public Page<Account> findAllByFilter(AccountFilterRequest filter, Pageable pageable) {
        List<Account> accounts = accountRepository.findAll();
        if (filter.getFullName() != null) {
            Predicate<Account> byFullName = (account) -> (account.getLastName() + " " + account.getFirstName()).toUpperCase()
                    .contains(filter.getFullName().replaceAll("\\s\\s+", " ").trim().toUpperCase());
            accounts = accounts.stream().filter(byFullName).collect(Collectors.toList());
        }
        if (filter.getUsername() != null) {
            Predicate<Account> byUsername = (account) -> (account.getUsername().toUpperCase()
                    .contains(filter.getUsername().replaceAll("\\s\\s+", " ").trim().toUpperCase()));
            accounts = accounts.stream().filter(byUsername).collect(Collectors.toList());
        }
        if (filter.getStatus() != null) {
            Predicate<Account> byStatus = (account) -> (account.getStatus()
                    .equalsIgnoreCase(filter.getStatus().replaceAll("\\s\\s+", " ").trim()));
            accounts = accounts.stream().filter(byStatus).collect(Collectors.toList());
        }
        if (filter.getEmail() != null) {
            Predicate<Account> byEmail = (account) -> (account.getEmail() != null && account.getEmail().toUpperCase()
                    .contains(filter.getEmail().replaceAll("\\s\\s+", " ").trim().toUpperCase()));
            accounts = accounts.stream().filter(byEmail).collect(Collectors.toList());
        }
        if (filter.getPhone() != null) {
            Predicate<Account> byPhone = (account) -> (account.getPhone() != null && account.getPhone()
                    .contains(filter.getPhone().trim()));
            accounts = accounts.stream().filter(byPhone).collect(Collectors.toList());
        }
        if (filter.getRole() != null) {
            Predicate<Account> byRole = (account) -> (account.getRole().getName()
                    .equalsIgnoreCase(filter.getRole().replaceAll("\\s\\s+", " ").trim()));
            accounts = accounts.stream().filter(byRole).collect(Collectors.toList());
        }
        if (filter.getId() != null) {
            Predicate<Account> byId = (account) -> (account.getId().toString().toUpperCase()
                    .contains(filter.getId().replaceAll("\\s\\s+", " ").trim().toUpperCase()));
            accounts = accounts.stream().filter(byId).collect(Collectors.toList());
        }
        if (accounts.size() == 0) {
            throw new NotFoundException("Empty List");
        }
        return new PageImpl<>(accounts);
    }


    @Override
    public Account findById(int id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElse(null);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void delete(int id) {
        Optional<Account> accountData = accountRepository.findById(id);
        Account account = accountData.get();
        account.setStatus(Status.INACTIVE.name());
        accountRepository.save(account);
    }

    @Override
    public void update(Account theEntity) {
        accountRepository.save(theEntity);
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
    public List<Account> findByRoleId(int id) {
        return accountRepository.findByRoleId(id);
    }

    @Override
    public boolean isDuplicated(String username) {
        return accountRepository.getAccountByUsername(username) != null;
    }

    @Override
    public List<Account> searchAccountsByFullName(String search, Pageable pageable) {
        return accountRepository.searchAccountsByFullName(search, pageable);
    }
}
