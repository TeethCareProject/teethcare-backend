package com.teethcare.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class FCMTokenStoreKey implements Serializable {
    @Column(name = "fcm_token")
    private String fcmToken;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
