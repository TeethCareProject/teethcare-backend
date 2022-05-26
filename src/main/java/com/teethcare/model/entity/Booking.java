package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

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

    @OneToOne
    @JoinColumn(name = "pre_booking_id", referencedColumnName = "id")
    private Booking preBooking;

    @OneToOne
    @JoinColumn(name = "post_booking_id", referencedColumnName = "id")
    private Booking postBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "account_id")
    private Patient patient;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "create_booking_date")
    private Date createBookingDate;

    @Column(name = "examination_time")
    private Timestamp examinationTime;

    @Column(name = "description")
    private String description;

    @Column(name = "appointment_date")
    private Timestamp appointmentDate;

    @Column(name = "expire_appointment_date")
    private Date expireAppointmentDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "dentist_id", referencedColumnName = "account_id")
    private Dentist dentist;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "note")
    private String note;

    @Column(name = "desiredCheckingTime")
    private String desiredCheckingTime;

    @ManyToOne
    @JoinColumn(name = "customer_service_id", referencedColumnName = "account_id")
    private CustomerService customerService;
}
