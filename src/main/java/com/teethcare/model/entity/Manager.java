package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "customer_service")
@DiscriminatorValue("1")
public class Manager extends Account {


}
