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

    Account findAccountByUsername(String username);

    Account getAccountByUsernameAndStatusIsNot(String username, String status);

    List<Account> findByRoleId(int roleId);

    List<Account> findAllByStatusIsNotNull(Pageable pageable);

    @Query("SELECT a FROM Account a WHERE CONCAT(a.lastName, ' ', a.firstName) LIKE %?1%")
    List<Account> searchAccountsByFullName(String search, Pageable pageable);
}
