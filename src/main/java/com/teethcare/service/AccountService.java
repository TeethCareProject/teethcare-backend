package com.teethcare.service;

import com.teethcare.model.entity.Account;

public interface AccountService {
    Account getAccountByUsername(String username);
}
