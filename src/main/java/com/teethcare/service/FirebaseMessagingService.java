package com.teethcare.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.model.request.NotificationMsgRequest;

import javax.management.BadAttributeValueExpException;

public interface FirebaseMessagingService {
    void sendNotification(NotificationMsgRequest notificationMsgRequest)
            throws FirebaseMessagingException;
    public void sendNotification(int bookingId, String title, String body, String role)
            throws FirebaseMessagingException, BadAttributeValueExpException;

    void addNewToken(String fcmToken, String jwtToken);

    void sendNotificationToCSByBookingId(int bookingId) throws FirebaseMessagingException;
}
