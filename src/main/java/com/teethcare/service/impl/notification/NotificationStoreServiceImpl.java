package com.teethcare.service.impl.notification;

import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.NotificationMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.NotificationStoreRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.NotificationStoreService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public void addNew(Account account, NotificationMsgRequest notificationMsgRequest) {
        NotificationStore notificationStore = notificationMapper.mapNotificationMsgRequestToNotificationStore(notificationMsgRequest);
        notificationStore.setAccount(account);
        notificationStore.setTime(new Timestamp(new Date().getTime()));
        notificationStore.setIsMarkedAsRead(false);
        notificationStore.setType(notificationMsgRequest.getType());
        notificationStoreRepository.save(notificationStore);
    }

    @Override
    public Page<NotificationStore> findAllByAccount(String jwtToken, Pageable pageable) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        List<NotificationStore> notificationStores = notificationStoreRepository.findAllByAccount(account, pageable.getSort());
        return PaginationAndSortFactory.convertToPage(notificationStores, pageable);
    }

    @Override
    public NotificationStore markAsRead(String jwtToken, int id) {
        NotificationStore notificationStore = notificationStoreRepository.findById(id);
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        if (notificationStore.getAccount().getId() == account.getId()) {
            notificationStore.setIsMarkedAsRead(true);
            notificationStoreRepository.save(notificationStore);
            return notificationStore;
        }
        throw new NotFoundException("Notification not found");
    }

    @Override
    public void markAllAsRead(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        List<NotificationStore> notificationStores = notificationStoreRepository.findAllByAccount(account);
        if (!notificationStores.isEmpty()) {
            notificationStores.stream().forEach(notificationStore -> {
                notificationStore.setIsMarkedAsRead(true);
                notificationStoreRepository.save(notificationStore);
            });
        }
    }

    @Override
    public Integer getNumsOfUnread(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        return notificationStoreRepository.countAllByIsMarkedAsReadAndAccount(false, account);
    }


}
