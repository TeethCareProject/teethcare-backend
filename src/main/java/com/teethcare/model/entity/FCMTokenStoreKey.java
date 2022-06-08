package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FCMTokenStoreKey implements Serializable {
    private String fcmToken;
    private Integer account;
}
