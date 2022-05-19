package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsername(String username);
}
