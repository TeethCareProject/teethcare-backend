package com.teethcare.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class NotificationDTO {
    private Integer accountId;
    private String title;
    private String body;
    private String url;
    private String image;
    private String type;
}
