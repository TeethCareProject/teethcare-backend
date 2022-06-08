package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationStoreService {
    void addNew(Account account, NotificationMsgRequest notificationMsgRequest);
    List<NotificationStore> findAllByAccount(String jwtToken);
}
