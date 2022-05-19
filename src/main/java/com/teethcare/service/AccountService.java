package com.teethcare.service;

import com.teethcare.model.entity.Account;

public interface AccountService {
    public Account getAccountByUsername(String username);
}
