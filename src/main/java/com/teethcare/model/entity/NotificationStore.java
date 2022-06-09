package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "notification_store")
public class NotificationStore {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "time")
    private Timestamp time;

    @Column(name = "url")
    private String url;

    @Column(name = "is_marked_as_read")
    private Boolean isMarkedAsRead;

    @Column(name = "type")
    private String type;
}
