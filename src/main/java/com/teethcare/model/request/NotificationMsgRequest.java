package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotificationMsgRequest {
    private String subject;
    private String content;
    private Map<String, String> data;
    private String image;
}
