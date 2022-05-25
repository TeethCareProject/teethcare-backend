package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsernameAndStatus(String username, String status);
    Account findAccountByUsername(String username);
    Account getAccountByUsernameAndStatusIsNot(String username, String status);
    List<Account> findByRoleId(int roleId);
}
