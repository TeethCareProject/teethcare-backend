package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_service")
public class Manager {
    @Id
    @Column(name = "account_id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

}
