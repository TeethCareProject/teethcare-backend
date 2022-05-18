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
@Entity
@Table(name = "application_owner")
public class ApplicationOwner {
    @Id
    @Column(name = "account_id")
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;
}
