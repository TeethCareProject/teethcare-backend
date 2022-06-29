package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "preBooking")
    @JsonBackReference
    private List<Booking> mappedPreBooking;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_booking_id", referencedColumnName = "id")
    @JsonManagedReference
    private Booking preBooking;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonManagedReference
    private Patient patient;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "create_booking_time")
    private Timestamp createBookingTime;

    @Column(name = "examination_time")
    private Timestamp examinationTime;

    @Column(name = "note")
    private String note;

    @Column(name = "appointment_date")
    private Timestamp appointmentDate;

    @Column(name = "expire_appointment_date")
    private Timestamp expireAppointmentDate;

    @Column(name = "appointment_status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    @ManyToMany
    @JoinTable(name = "booking_detail",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    @JsonManagedReference
    private List<ServiceOfClinic> services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    @JsonManagedReference
    private Dentist dentist;
}
