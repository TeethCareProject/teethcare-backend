package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "account_id")
public class Patient extends Account {
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @Column(nullable = true)
    @JsonBackReference
    private List<Booking> bookingList;

    @OneToOne
    private Location location;
}
