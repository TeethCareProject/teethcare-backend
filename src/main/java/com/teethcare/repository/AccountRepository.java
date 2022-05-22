package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findAccountByUsernameAndStatus(String username, String status);
}
