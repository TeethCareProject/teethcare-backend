package com.teethcare.service;

import com.teethcare.model.entity.Account;
import org.springframework.stereotype.Service;


public interface AccountService extends CRUDService<Account> {
    Account getAccountByUsername(String username);
    Account getActiveAccountByUsername(String username);
}
