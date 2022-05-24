package com.teethcare.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "manager")
@PrimaryKeyJoinColumn(name = "account_id")
public class Manager extends Account {
//    @OneToOne(mappedBy = "manager")
//    private Clinic clinic;
}
