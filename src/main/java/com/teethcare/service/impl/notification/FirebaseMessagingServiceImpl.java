package com.teethcare.service.impl.notification;

import com.google.firebase.messaging.*;
import com.teethcare.common.NotificationTemplate;
import com.teethcare.common.NotificationType;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.repository.FCMTokenStoreRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.BookingService;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.NotificationStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private FCMTokenStoreRepository fcmTokenStoreRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private NotificationStoreService notificationStoreService;
    @Autowired
    private BookingService bookingService;

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

            // FE requires Type as Title
            Notification notification = Notification.builder()
                    .setTitle(notificationMsgRequest.getType())
                    .setBody(notificationMsgRequest.getBody())
                    .setImage(notificationMsgRequest.getImage())
                    .build();

            WebpushNotification webpushNotification = WebpushNotification.builder()
                    .setTitle(notificationMsgRequest.getTitle())
                    .setBody(notificationMsgRequest.getBody())
                    .setImage(notificationMsgRequest.getImage())
                    .build();

            List<Message> messages = new ArrayList<>();
            for (String token : fcmTokens) {
                messages.add(Message.builder()
                        .setToken(token.trim())
                        .setNotification(notification)
                        .setWebpushConfig(WebpushConfig
                                .builder()
                                .setNotification(webpushNotification)
                                .build())
                        .build());
            }
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
        notificationMsgRequest.setBody(String.valueOf(bookingId));
        this.sendNotification(notificationMsgRequest);
    }

    public void sendNotification(int bookingId, String title, String body, String role)
            throws FirebaseMessagingException, BadAttributeValueExpException {
        Booking booking = bookingService.findBookingById(bookingId);
        int accountId;
        switch (com.teethcare.common.Role.valueOf(role)) {
            case PATIENT:
                accountId = booking.getPatient().getId();
                break;
            case DENTIST:
                accountId = booking.getDentist().getId();
                break;
            default:
                throw new BadAttributeValueExpException(role);
        }

        NotificationMsgRequest confirmNotification =
                NotificationMsgRequest.builder()
                        .accountId(accountId)
                        .title(title)
                        .body(body)
                        .build();

        this.sendNotification(confirmNotification);
    }

    @Override
    public void sendNotificationToManagerByClinic(Clinic clinic, String title, String body) throws FirebaseMessagingException {
        Account manager = clinic.getManager();
        NotificationMsgRequest deleteVoucher =
                NotificationMsgRequest.builder()
                        .accountId(manager.getId())
                        .title(title)
                        .body(body)
                        .build();
        this.sendNotification(deleteVoucher);
    }
}
