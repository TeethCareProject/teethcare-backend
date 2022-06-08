package com.teethcare.service.impl.notification;

import com.google.firebase.messaging.*;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.FCMTokenStore;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.FCMTokenStoreRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.NotificationStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {


    private final JwtTokenUtil jwtTokenUtil;
    private final FCMTokenStoreRepository fcmTokenStoreRepository;
    private final AccountService accountService;
    private final NotificationStoreService notificationStoreService;

    public void sendNotification(NotificationMsgRequest notificationMsgRequest) throws FirebaseMessagingException {
        Account account = accountService.findById(notificationMsgRequest.getAccountId());
        List<FCMTokenStore> fcmTokenStores = fcmTokenStoreRepository.findAllByAccount(account);
        List<String> fcmTokens = fcmTokenStores.stream().map(FCMTokenStore::getFcmToken).collect(Collectors.toList());


        Notification notification = Notification.builder()
                .setTitle(notificationMsgRequest.getTitle())
                .setBody(notificationMsgRequest.getBody())
                .setImage(notificationMsgRequest.getImage())
                .build();

        WebpushNotification webpushNotification = WebpushNotification.builder()
                .setTitle(notificationMsgRequest.getTitle())
                .setBody(notificationMsgRequest.getBody())
                .setImage(notificationMsgRequest.getImage())
                .build();

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(fcmTokens)
                .setNotification(notification)
                .setWebpushConfig(WebpushConfig
                        .builder()
                        .setNotification(webpushNotification)
                        .setFcmOptions(WebpushFcmOptions.builder().setLink(notificationMsgRequest.getUrl()).build())
                        .build())
                .build();
        notificationStoreService.addNew(account, notificationMsgRequest);
        FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
    }

    public void addNewToken(String fcmToken, String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        FCMTokenStore fcmTokenStore = fcmTokenStoreRepository.findByAccountAndFcmToken(account, fcmToken);
        if (fcmTokenStore != null) {
            throw new BadRequestException("Existed!");
        }
        FCMTokenStore notification = new FCMTokenStore(fcmToken, account);
        fcmTokenStoreRepository.save(notification);
    }

}
