package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationStoreService {
    void addNew(Account account, NotificationMsgRequest notificationMsgRequest);

    Page<NotificationStore> findAllByAccount(String jwtToken, Pageable pageable);

    NotificationStore markAsRead(String jwtToken, int id);
    void markAllAsRead(String jwtToken);

    Integer getNumsOfUnread(String jwtToken);
}
