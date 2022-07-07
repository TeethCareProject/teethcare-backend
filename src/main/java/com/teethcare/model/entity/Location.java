package com.teethcare.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "location")
public class Location {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address_string")
    private String addressString;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @OneToOne
    @JoinColumn(name = "ward_id", referencedColumnName = "id")
    Ward ward;
//
//    @OneToOne(mappedBy = "location")
//    @Column(nullable = true)
//    @JsonBackReference
//    Clinic clinic;
}
