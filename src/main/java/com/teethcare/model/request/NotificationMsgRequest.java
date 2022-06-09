package com.teethcare.model.request;

import com.teethcare.utils.Trimmable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NotificationMsgRequest implements Trimmable {
    @NotNull
    Integer accountId;
    @NotNull
    private String title;
    @NotNull
    private String body;
    private String url;
    private String image;
    private String type;
}
