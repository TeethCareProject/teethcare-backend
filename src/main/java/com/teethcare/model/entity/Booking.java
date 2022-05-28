package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "preBooking")
    @JsonBackReference
    private Booking mappedPreBooking;

   /* @OneToOne(fetch = FetchType.LAZY, mappedBy = "postBooking")
    @JsonBackReference
    private Booking mappedPostBooking;*/

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_booking_id", referencedColumnName = "id")
    @JsonManagedReference
    private Booking preBooking;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_booking_id", referencedColumnName = "id")
    @JsonManagedReference
    private Booking postBooking; */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonManagedReference
    private Patient patient;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "create_booking_date")
    private Date createBookingDate;

    @Column(name = "examination_time")
    private Timestamp examinationTime;

    @Column(name = "description")
    private String description;

    @Column(name = "appointment_date")
    private Timestamp appointmentDate;

    @Column(name = "expire_appointment_date")
    private Timestamp expireAppointmentDate;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    @JsonManagedReference
    private Dentist dentist;

    @Column(name = "note")
    private String note;

    @Column(name = "desired_checking_time")
    private Timestamp desiredCheckingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_service_id")
    @JsonManagedReference
    private CustomerService customerService;

    @ManyToMany
    @JoinTable(name = "booking_detail",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    @JsonManagedReference
    private List<ServiceOfClinic> serviceOfClinics;

}
