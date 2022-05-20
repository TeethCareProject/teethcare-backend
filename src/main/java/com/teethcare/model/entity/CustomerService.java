package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer_service")
@PrimaryKeyJoinColumn(name="account_id")
public class CustomerService extends Account {

//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(name = "clinic_id")
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}
