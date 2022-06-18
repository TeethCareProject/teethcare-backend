package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.FCMTokenStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FCMTokenStoreRepository extends JpaRepository<FCMTokenStore, String> {
    List<FCMTokenStore> findAllByAccount(Account account);

    FCMTokenStore findByAccountAndFcmToken(Account account, String fcmToken);
}
