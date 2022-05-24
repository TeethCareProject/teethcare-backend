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
@Entity(name = "dentist")
@PrimaryKeyJoinColumn(name = "account_id")
public class Dentist extends Account {

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;
}
