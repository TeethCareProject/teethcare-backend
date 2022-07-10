package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
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
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToOne(cascade = CascadeType.ALL)
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

    @Column(name = "start_time_shift_1")
    private Time startTimeShift1;

    @Column(name = "end_time_shift_1")
    private Time endTimeShift1;

    @Column(name = "start_time_shift_2")
    private Time startTimeShift2;

    @Column(name = "end_time_shift_2")
    private Time endTimeShift2;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "booking_gap")
    private Integer bookingGap;

    @Column(name = "facebook_page_id")
    private String facebookPageId;

    @Column(name = "expired_day")
    private Integer expiredDay = 3;

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
