package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Account manager;

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "avg_rating_score")
    private Float avgRatingScore;

    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic")
    private List<Dentist> dentists;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic")
    private List<CustomerService> customerServices;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic")
    private List<ServiceOfClinic> serviceOfClinic;
}
