package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dentist")
@DiscriminatorValue("4")
public class Dentist extends Account {

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "phone_number")
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @ManyToOne
    private Clinic clinic;
}