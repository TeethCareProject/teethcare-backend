package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;

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
