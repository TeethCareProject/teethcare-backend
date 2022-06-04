package com.teethcare.service;

import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.AccountFilterRequest;
import com.teethcare.model.request.AccountUpdateStatusRequest;
import com.teethcare.repository.AccountRepository;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        accounts = accounts.stream().filter(filter.getPredicate()).collect(Collectors.toList());
        return PaginationAndSortFactory.convertToPage(accounts, pageable);
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
        account.setStatus(Status.Account.INACTIVE.name());
        accountRepository.save(account);
    }

    @Override
    public void update(Account theEntity) {
        accountRepository.save(theEntity);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }


    @Override
    public Account getActiveAccountByUsername(String username) {
        return accountRepository.findAccountByUsernameAndStatus(username, Status.Account.ACTIVE.name());
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

    @Override
    public void updateStatus(AccountUpdateStatusRequest accountUpdateStatusRequest, int id) {
        String status = accountUpdateStatusRequest.getStatus();
        if (status != null
                && (status.toUpperCase().trim().equals(Status.Account.INACTIVE.name())
                || status.toUpperCase().trim().equals(Status.Account.ACTIVE.name()))) {
            Account account = this.findById(id);
            if (account != null) {
                account.setStatus(status.toUpperCase().trim());
                this.update(account);
            } else {
                throw new NotFoundException("Account not found!");
            }
        } else {
            throw new BadRequestException(Message.INVALID_STATUS.name());
        }
    }
}
