package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUsernameAndStatus(String username, String status);

    Account findByUsername(String username);

    Account getAccountByUsername(String username);

    List<Account> findByRoleId(int roleId);

    Account findAccountsById(int id);

    List<Account> findAllByStatusIsNotNull(Pageable pageable);

    @Query("SELECT a FROM Account a WHERE CONCAT(a.lastName, ' ', a.firstName) LIKE %?1%")
    List<Account> searchAccountsByFullName(String search, Pageable pageable);

    Account findById(int id);
}
