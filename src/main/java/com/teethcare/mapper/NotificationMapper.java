package com.teethcare.mapper;

import com.teethcare.model.entity.NotificationStore;
import com.teethcare.model.request.NotificationMsgRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface NotificationMapper {
    NotificationStore mapNotificationMsgRequestToNotificationStore(NotificationMsgRequest notificationMsgRequest);
}
