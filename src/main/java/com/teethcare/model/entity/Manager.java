package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "account_id")
public class Manager extends Account {


}
