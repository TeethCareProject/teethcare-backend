package com.teethcare.model.request;

import com.teethcare.utils.Trimmable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationMsgRequest implements Trimmable {
    Integer accountId;
    private String title;
    private String body;
    private String url;
    private String image;
    private String type;
}
