package com.teethcare.service.impl.notification;

import com.google.firebase.messaging.*;
import com.teethcare.common.NotificationTemplate;
import com.teethcare.common.NotificationType;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.FCMTokenStore;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.FCMTokenStoreRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.BookingService;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.NotificationStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {


    private final JwtTokenUtil jwtTokenUtil;
    private final FCMTokenStoreRepository fcmTokenStoreRepository;
    private final AccountService accountService;
    private final NotificationStoreService notificationStoreService;
    private final BookingService bookingService;

    @Override
    public void sendNotification(NotificationMsgRequest notificationMsgRequest) throws FirebaseMessagingException {
        notificationMsgRequest.setType(notificationMsgRequest.getType() == null ? NotificationType.NORMAL_NOTIFICATION.name() : notificationMsgRequest.getType());
        Account account = accountService.findById(notificationMsgRequest.getAccountId());
        if (account == null) {
            throw new NotFoundException("User ID " + notificationMsgRequest.getAccountId() + " not found");
        }
        List<FCMTokenStore> fcmTokenStores = fcmTokenStoreRepository.findAllByAccount(account);
        if (!fcmTokenStores.isEmpty()) {
            List<String> fcmTokens = fcmTokenStores.stream().map(FCMTokenStore::getFcmToken).collect(Collectors.toList());
            System.out.println(fcmTokens.get(0).getClass());

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

//            MulticastMessage multicastMessage = MulticastMessage.builder()
//                    .addAllTokens(fcmTokens)
//                    .setNotification(notification)
//                    .setWebpushConfig(WebpushConfig
//                            .builder()
//                            .setNotification(webpushNotification)
//                            .setFcmOptions(WebpushFcmOptions.builder().setLink(notificationMsgRequest.getUrl()).build())
//                            .build())
//                    .build();
            List<Message> messages = new ArrayList<>();
            for (String token : fcmTokens) {
                System.out.println(token);
//                Message message = Message.builder().setToken("cWs2s6ADUFiuFuDKef1ksd:APA91bFYeEvgqUMssQbnweHsXJ9TX5QOZilEaHudcGoJ-6sch8j5PAaWwfcl3QjRD4ovpLe1c2V5NRFTLg19fDiB2uTWIkoFSriVMN7DtDVVo1pAhvObSO0qq7o4rM0apxaOdpb1UGIV").setNotification(notification).build();
//                FirebaseMessaging.getInstance().send(message);
                messages.add(Message.builder().setToken(token.trim()).setNotification(notification).setWebpushConfig(WebpushConfig.builder().setNotification(webpushNotification).build()).build());
            }
//            System.out.println("cWs2s6ADUFiuFuDKef1ksd:APA91bFYeEvgqUMssQbnweHsXJ9TX5QOZilEaHudcGoJ-6sch8j5PAaWwfcl3QjRD4ovpLe1c2V5NRFTLg19fDiB2uTWIkoFSriVMN7DtDVVo1pAhvObSO0qq7o4rM0apxaOdpb1UGIV".equals(fcmTokens.get(0).trim()));
//            System.out.println("cWs2s6ADUFiuFuDKef1ksd:APA91bFYeEvgqUMssQbnweHsXJ9TX5QOZilEaHudcGoJ-6sch8j5PAaWwfcl3QjRD4ovpLe1c2V5NRFTLg19fDiB2uTWIkoFSriVMN7DtDVVo1pAhvObSO0qq7o4rM0apxaOdpb1UGIV");
//            System.out.println(fcmTokens.get(0));
//            Message message = Message.builder().setToken("cWs2s6ADUFiuFuDKef1ksd:APA91bFYeEvgqUMssQbnweHsXJ9TX5QOZilEaHudcGoJ-6sch8j5PAaWwfcl3QjRD4ovpLe1c2V5NRFTLg19fDiB2uTWIkoFSriVMN7DtDVVo1pAhvObSO0qq7o4rM0apxaOdpb1UGIV").setNotification(notification).build();
//            Message message = Message.builder().setToken(fcmTokens.get(0)).setNotification(notification).build();
//            FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
            FirebaseMessaging.getInstance().sendAll(messages);
            notificationStoreService.addNew(account, notificationMsgRequest);
        } else {
            notificationStoreService.addNew(account, notificationMsgRequest);
        }

    }

    @Override
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

    @Override
    public void sendNotificationToCSByBookingId(int bookingId) throws FirebaseMessagingException {
        Booking booking = bookingService.findBookingById(bookingId);
        CustomerService customerService = booking.getCustomerService();
        NotificationMsgRequest notificationMsgRequest = NotificationTemplate.CHECK_IN_NOTIFICATION;
        notificationMsgRequest.setAccountId(customerService.getId());
        this.sendNotification(notificationMsgRequest);
    }
}
