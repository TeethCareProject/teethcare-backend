package com.teethcare.model.response;

import com.teethcare.model.entity.NotificationStore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class NotificationListResponse {
    Page<NotificationStore> notificationStores;
    Integer numsOfUnread;
}
