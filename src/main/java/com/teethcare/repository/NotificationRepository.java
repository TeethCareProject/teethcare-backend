package com.teethcare.repository;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByAccount(Account account);
}
