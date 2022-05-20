package com.teethcare.service;

import com.teethcare.model.entity.Account;

import java.util.List;


public interface AccountService {
    public Account getAccountByUsername(String username);

   String getActiveAccountByUsername(String username);
}
