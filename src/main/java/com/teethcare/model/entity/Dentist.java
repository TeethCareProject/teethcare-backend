package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dentist")
@PrimaryKeyJoinColumn(name = "account_id")
public class Dentist extends Account {

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    @JsonBackReference
    private Clinic clinic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dentist")
    @Column(nullable = true)
    @JsonBackReference
    private List<Booking> bookingList;
}
