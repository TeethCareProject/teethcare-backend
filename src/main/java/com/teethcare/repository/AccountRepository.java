package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< Updated upstream
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
=======
public interface AccountRepository extends JpaRepository<Account, Integer> {
>>>>>>> Stashed changes
    Account findAccountByUsername(String username);
}
