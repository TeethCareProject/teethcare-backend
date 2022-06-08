package com.teethcare.service.impl.notification;

import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.NotificationMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.NotificationStoreRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.NotificationStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationStoreServiceImpl implements NotificationStoreService {
    private final NotificationStoreRepository notificationStoreRepository;
    private final NotificationMapper notificationMapper;
    private final AccountService accountService;
    private final JwtTokenUtil jwtTokenUtil;

    public void addNew(Account account, NotificationMsgRequest notificationMsgRequest) {
        NotificationStore notificationStore = notificationMapper.mapNotificationMsgRequestToNotificationStore(notificationMsgRequest);
        notificationStore.setAccount(account);
        notificationStore.setTime(new Timestamp(new Date().getTime()));
        notificationStoreRepository.save(notificationStore);
    }

    public List<NotificationStore> findAllByAccount(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        return notificationStoreRepository.findAllByAccount(account);
    }


}
