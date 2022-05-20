package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "application_owner")
@PrimaryKeyJoinColumn(name = "account_id")
public class ApplicationOwner extends Account {

}
