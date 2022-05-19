package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository1 extends JpaRepository<Account, String> {
}