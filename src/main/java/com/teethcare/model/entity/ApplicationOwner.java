package com.teethcare.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "application_owner")
@PrimaryKeyJoinColumn(name = "account_id")
public class ApplicationOwner extends Account {

}
