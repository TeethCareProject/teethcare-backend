package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "account_id")
public class Patient extends Account {
    @Email
    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "phone_number")
    @Size(min = 10, max = 10)
    private String phoneNumber;
}
