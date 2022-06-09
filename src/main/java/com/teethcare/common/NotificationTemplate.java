package com.teethcare.common;

import com.teethcare.model.request.NotificationMsgRequest;

public class NotificationTemplate {
    public static NotificationMsgRequest CHECK_IN_NOTIFICATION = new NotificationMsgRequest(null, "Open Booking Detail", "Click to open the booking detail!", "/", null, NotificationType.OPEN_BOOKING_NOTIFICATION.name());
}