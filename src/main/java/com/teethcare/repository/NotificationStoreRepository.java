package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.NotificationStore;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationStoreRepository extends JpaRepository<NotificationStore, Integer> {
    List<NotificationStore> findAllByAccount(Account account, Sort sort);
    List<NotificationStore> findAllByAccount(Account account);

    NotificationStore findById(int id);

    Integer countAllByIsMarkedAsReadAndAccount(boolean isMarkedAsRead, Account account);
}
