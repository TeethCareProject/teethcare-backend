package com.teethcare.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.NotificationStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstant.Notification.NOTIFICATION_ENDPOINT)
public class NotificationController {
    private final FirebaseMessagingService firebaseMessagingService;
    private final NotificationStoreService notificationStoreService;

    @PostMapping
    public ResponseEntity<Message> sendNotification(@RequestBody NotificationMsgRequest notificationMsgRequest) throws FirebaseMessagingException {
        firebaseMessagingService.sendNotification(notificationMsgRequest);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<NotificationStore>> getAllByAccount(@RequestHeader(value = AUTHORIZATION) String authorHeader) {
        return new ResponseEntity<>(notificationStoreService.findAllByAccount(authorHeader.substring("Bearer ".length())), HttpStatus.OK);
    }
}
