package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "account_id")
public class Manager extends Account {


}
