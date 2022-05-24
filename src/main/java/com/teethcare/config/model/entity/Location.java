package com.teethcare.config.model.entity;


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
    private Integer id;

    @Column(name = "address_string")
    private String addressString;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longtitude")
    private Float longitude;

    @OneToOne
    @JoinColumn(name = "ward_id", referencedColumnName = "id")
    Ward ward;
}
