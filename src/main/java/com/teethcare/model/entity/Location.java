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
@Entity(name = "location")
public class Location {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address_string")
    private String addressString;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longtitude")
    private Double longitude;

    @OneToOne
    @JoinColumn(name = "ward_id", referencedColumnName = "id")
    Ward ward;

    public String getFullAddress() {
        return addressString + ", " + ward.getName() +
                ", " + ward.getDistrict().getName() +
                ", " + ward.getDistrict().getProvince().getName();
    }
}
