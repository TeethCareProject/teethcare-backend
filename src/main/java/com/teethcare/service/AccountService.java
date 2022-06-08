package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.request.AccountFilterRequest;
import com.teethcare.model.request.AccountUpdateStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountService extends CRUDService<Account> {
    Account getAccountByUsername(String username);

    Account getActiveAccountByUsername(String username);
    Account findById(int id);

    boolean isDuplicated(String username);

    List<Account> findByRoleId(int id);

    List<Account> findAllAccounts(Pageable pageable);

    List<Account> searchAccountsByFullName(String search, Pageable pageable);

    Page<Account> findAllByFilter(AccountFilterRequest filter, Pageable pageable);

    void updateStatus(AccountUpdateStatusRequest accountUpdateStatusRequest, int id);

}
