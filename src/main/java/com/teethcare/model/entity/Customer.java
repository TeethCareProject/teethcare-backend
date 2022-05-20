package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "account_id")
public class Customer extends Account {
    @Email
    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "phone_number")
    @Size(min = 10, max = 10)
    private String phoneNumber;
}
