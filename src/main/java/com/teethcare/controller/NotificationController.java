package com.teethcare.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import com.teethcare.model.response.NotificationListResponse;
import com.teethcare.service.FirebaseMessagingService;
import com.teethcare.service.NotificationStoreService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<NotificationListResponse> getAllByAccount(@RequestHeader(value = AUTHORIZATION) String authorHeader,
                                                                    @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(name = "size", required = false) Integer size,
                                                                    @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                    @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<NotificationStore> notificationStoreList = notificationStoreService.findAllByAccount(authorHeader.substring("Bearer ".length()), pageable);
        NotificationListResponse notificationListResponse = new NotificationListResponse(notificationStoreList, notificationStoreService.getNumsOfUnread(authorHeader.substring("Bearer ".length())));
        return new ResponseEntity<>(notificationListResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/read")
    public ResponseEntity<NotificationStore> markAsRead(@RequestHeader(value = AUTHORIZATION) String authorHeader,
                                                        @PathVariable("id") int id) {
        return new ResponseEntity<>(notificationStoreService.markAsRead(authorHeader.substring("Bearer ".length()), id), HttpStatus.OK);
    }
}
