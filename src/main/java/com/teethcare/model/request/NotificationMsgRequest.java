package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationMsgRequest {
    int accountId;
    private String title;
    private String body;
    private String url;
    private String image;
}
