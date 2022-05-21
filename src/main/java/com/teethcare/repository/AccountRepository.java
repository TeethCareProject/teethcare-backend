package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsername(String username);

    Account getAccountByUsernameAndAndStatusIsNot(String username, int status);

}
