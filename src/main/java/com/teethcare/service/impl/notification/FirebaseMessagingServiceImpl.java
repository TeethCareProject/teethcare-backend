package com.teethcare.service.impl.notification;

import com.google.firebase.messaging.*;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.NotificationRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import com.teethcare.model.entity.FCMTokenStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {


    private final JwtTokenUtil jwtTokenUtil;
    private final NotificationRepository notificationRepository;
    private final AccountService accountService;

    public void sendNotification(NotificationMsgRequest notificationMsgRequest, String jwtToken) throws FirebaseMessagingException {

        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        List<FCMTokenStore> fcmTokenStores = notificationRepository.findAllByAccount(account);
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

        FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
    }

    public void addNewToken(String fcmToken, String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        FCMTokenStore notification = new FCMTokenStore(fcmToken, account);
        notificationRepository.save(notification);
    }

}
