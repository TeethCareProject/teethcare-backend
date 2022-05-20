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
    @Query(
            value = "SELECT c.username FROM account c WHERE c.status = true AND c.username = :username")
    ArrayList<Account> getActiveAccountByUsername(@Param("username") String username);
}
