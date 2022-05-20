package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsername(String username);

    @Query(value = "SELECT a.username FROM account a WHERE a.status = 1 and a.username = ?1")
    String getActiveUserName(String username);

    Account getAccountByUsernameAndAndStatus(String username, int status);

}
