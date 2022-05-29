package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer_service")
@PrimaryKeyJoinColumn(name = "account_id")
public class CustomerService extends Account {

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "clinic_id")
    @JsonBackReference
    private Clinic clinic;

    @OneToMany(mappedBy = "customerService", fetch = FetchType.LAZY)
    @Column(nullable = true)
    @JsonBackReference
    private List<Booking> bookingList;

}
