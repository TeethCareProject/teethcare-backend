package com.teethcare.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.model.request.FCMTokenRequest;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping(EndpointConstant.Notification.FCM_TOKEN_ENDPOINT)
    public ResponseEntity<Message> addNewToken(@RequestBody FCMTokenRequest fcmTokenRequest,
                                               @RequestHeader(value = AUTHORIZATION) String authorHeader) {
        firebaseMessagingService.addNewToken(fcmTokenRequest.getFcmToken(), authorHeader.substring("Bearer ".length()));
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }

    @PostMapping(EndpointConstant.Notification.NOTIFICATION_ENDPOINT)
    public ResponseEntity<Message> sendNotification(@RequestBody NotificationMsgRequest notificationMsgRequest,
                                                   @RequestHeader(value = AUTHORIZATION) String authorHeader) throws FirebaseMessagingException {
        firebaseMessagingService.sendNotification(notificationMsgRequest, authorHeader.substring("Bearer ".length()));
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
