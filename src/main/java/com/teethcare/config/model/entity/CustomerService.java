package com.teethcare.config.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customer_service")
@PrimaryKeyJoinColumn(name = "account_id")
public class CustomerService extends Account {

    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;
}
