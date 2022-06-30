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
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    @JsonManagedReference
    private Dentist dentist;

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

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "discount_value")
    private String discountValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_service_id")
    @JsonManagedReference
    private CustomerService customerService;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "order")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
