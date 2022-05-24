package com.teethcare.config.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "province")
public class Province {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}
