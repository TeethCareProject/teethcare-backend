package com.teethcare.service;

import com.teethcare.model.entity.Account;

import java.util.List;

public interface AccountService extends CRUDService<Account>{
    List<Account> findByRoleId(int id);
}
