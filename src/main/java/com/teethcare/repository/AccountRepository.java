package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsername(String username);

    @Query(value = "SELECT a.username FROM account a WHERE a.status = 1 and a.username = ?1")
    String getActiveUserName(String username);
}
