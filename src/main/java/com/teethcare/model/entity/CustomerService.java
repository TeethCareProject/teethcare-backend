package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customer_service")
@PrimaryKeyJoinColumn(name = "account_id")
public class CustomerService  extends Account{

    @ManyToOne
    private Clinic clinic;
}
