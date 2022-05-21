package com.teethcare.service;

import com.teethcare.model.entity.Account;


public interface AccountService extends CRUDService<Account> {
    Account getAccountByUsername(String username);

    Account getActiveAccountByUsername(String username);

    boolean isDuplicated(String username, String status);

}
