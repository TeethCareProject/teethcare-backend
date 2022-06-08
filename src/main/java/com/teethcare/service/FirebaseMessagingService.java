package com.teethcare.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.model.request.NotificationMsgRequest;

public interface FirebaseMessagingService {
    void sendNotification(NotificationMsgRequest notificationMsgRequest) throws FirebaseMessagingException;

    void addNewToken(String fcmToken, String jwtToken);
}
