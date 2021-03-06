package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "fcm_token_store")
@IdClass(FCMTokenStoreKey.class)
public class FCMTokenStore {
    @Id
    @Column(name = "fcm_token")
    private String fcmToken;
    @Id
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
