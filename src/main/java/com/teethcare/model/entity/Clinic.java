package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "manager_id", length = 72, nullable = false)
    @NotEmpty
    private String managerId;

    @Column(name = "location_id", nullable = false)
    @NotEmpty
    private String locationId;

    @NotEmpty
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "tax_code", length = 13, unique = true, nullable = false)
    private String taxCode;

    @Column(name = "avg_rating_score")
    @Max(value = 5)
    @Min(value = 0)
    private float avgRatingScore;

    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic")
    @JsonBackReference
    private List<Dentist> dentists;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic")
    @JsonBackReference
    private List<CustomerService> customerServices;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic")
    @JsonBackReference
    private List<ServiceOfClinic> serviceOfClinic;
}
