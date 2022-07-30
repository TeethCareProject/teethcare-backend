package com.teethcare.model.entity;

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
@Table(name = "[order]")
public class Order {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "patient_id")
    private int patientId;

    @Column(name = "patient_first_name")
    private String patientFirstName;

    @Column(name = "patient_last_name")
    private String patientLastName;

    @Column(name = "patient_phone")
    private String patientPhone;

    @Column(name = "patient_date_of_birth")
    private Timestamp patientDateOfBirth;

    @Column(name = "patient_gender")
    private String patientGender;

    @Column(name = "patient_email")
    private String patientEmail;

    @Column(name = "patient_address")
    private String patientAddress;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "create_booking_time")
    private Timestamp createBookingTime;

    @Column(name = "examination_time")
    private Timestamp examinationTime;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "dentist_id")
    private int dentistId;

    @Column(name = "dentist_first_name")
    private String dentistFirstName;

    @Column(name = "dentist_last_name")
    private String dentistLastName;

    @Column(name = "note")
    private String note;

    @Column(name = "clinic_name")
    private String clinicName;

    @Column(name = "clinic_tax_code")
    private String clinicTaxCode;

    @Column(name = "clinic_location")
    private String clinicLocation;

    @Column(name = "clinic_email")
    private String clinicEmail;

    @Column(name = "voucher_id")
    private int voucherId;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "discount_value")
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Column(name = "customer_service_id")
    private int customerServiceId;

    @Column(name = "clinic_id")
    private int clinicId;

    @Column(name = "clinic_phone")
    private String clinicPhone;

    @Column(name = "customer_service_first_name")
    private String customerServiceFirstName;

    @Column(name = "customer_service_last_name")
    private String customerServiceLastName;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,
            mappedBy = "order")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
