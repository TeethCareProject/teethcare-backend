package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "changed_password_key")
public class ForgotPasswordKey {

    @Id
    @Column(name = "key")
    private String key;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "status")
    private String status;

    @Column(name = "created_time")
    private Timestamp createdTime;
}
