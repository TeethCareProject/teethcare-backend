package com.teethcare.model.request;

import com.teethcare.utils.Trimmable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class NotificationMsgRequest implements Trimmable {
    private Integer accountId;
    private String title;
    private String body;
    private String url;
    private String image;
    private String type;
}
