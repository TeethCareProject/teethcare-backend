package com.teethcare.service;

import com.teethcare.model.entity.Account;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface AccountService extends CRUDService<Account> {
    Account getAccountByUsername(String username);

    Account getActiveAccountByUsername(String username);

    boolean isDuplicated(String username);

    List<Account> findByRoleId(int id);

    List<Account> findAllAccounts(Pageable pageable);
    List<Account> searchAccountsByFullName(String search, Pageable pageable);
}
