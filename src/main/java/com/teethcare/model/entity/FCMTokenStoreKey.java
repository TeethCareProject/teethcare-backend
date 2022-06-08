package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FCMTokenStoreKey implements Serializable {
    private String fcmToken;
    private Integer account;
}
