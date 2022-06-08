package com.teethcare.service.impl.notification;

import com.google.firebase.messaging.*;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.NotificationRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import com.teethcare.model.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        List<Notification> notifications = notificationRepository.findAllByAccount(account);
        List<String> fcmTokens = notifications.stream().map(Notification::getFcmToken).collect(Collectors.toList());

        WebpushNotification notification = WebpushNotification.builder()
                .setTimestampMillis(new Date().getTime())
                .setTitle(notificationMsgRequest.getSubject())
                .setBody(notificationMsgRequest.getContent())
                .build();
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(fcmTokens)
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(notification)
                        .setFcmOptions(WebpushFcmOptions.builder().setLink("/").build())
                        .build())
                .putAllData(notificationMsgRequest.getData()).build();

        FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
    }

    public void addNewToken(String fcmToken, String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        Notification notification = new Notification(fcmToken, account);
        notificationRepository.save(notification);
    }

}
