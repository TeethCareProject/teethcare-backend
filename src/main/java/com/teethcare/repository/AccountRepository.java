package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsername(String username);
    @Query(
            value = "SELECT * FROM Account a WHERE a.status <> 0 AND a.username = ?1",
            nativeQuery = true)
    Account getActiveAccountByUsername(String username);
}
