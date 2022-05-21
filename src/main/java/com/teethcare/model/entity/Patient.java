package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "account_id")
public class Patient extends Account {
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;
}
