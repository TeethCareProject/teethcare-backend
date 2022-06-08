package com.teethcare.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstant.Notification.NOTIFICATION_ENDPOINT)
public class NotificationController {
    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping
    public ResponseEntity<Message> sendNotification(@RequestBody NotificationMsgRequest notificationMsgRequest) throws FirebaseMessagingException {
        firebaseMessagingService.sendNotification(notificationMsgRequest);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
