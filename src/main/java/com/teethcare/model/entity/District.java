package com.teethcare.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "district")
public class District {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id")
    Province province;

    @JsonBackReference
    @OneToMany(mappedBy = "district")
    List<Ward> wards;
}
