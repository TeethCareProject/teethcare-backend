package com.teethcare.common;

import com.teethcare.model.request.NotificationMsgRequest;

public class NotificationTemplate {
    public static NotificationMsgRequest CHECK_IN_NOTIFICATION = new NotificationMsgRequest(null, NotificationType.OPEN_BOOKING_NOTIFICATION.name(), null, "/", null, NotificationType.OPEN_BOOKING_NOTIFICATION.name());
    public static NotificationMsgRequest UPDATE_BOOKING_1ST = new NotificationMsgRequest(null, NotificationType.UPDATE_BOOKING_1ST_NOTIFICATION.name(),
            null, "/", null, NotificationType.UPDATE_BOOKING_1ST_NOTIFICATION.name());
    public static NotificationMsgRequest UPDATE_BOOKING_2RD = new NotificationMsgRequest(null,
            NotificationType.UPDATE_BOOKING_2RD_NOTIFICATION.name(),
            null, "/", null, NotificationType.UPDATE_BOOKING_2RD_NOTIFICATION.name());
}