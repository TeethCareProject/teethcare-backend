package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "notification")
@AllArgsConstructor
public class Notification implements Serializable {
    @Id
    @Column(name = "fcm_token")
    private String fcmToken;

    @Id
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
