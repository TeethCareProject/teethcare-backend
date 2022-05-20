package com.teethcare.model.entity;

import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "manager_id", length = 72, nullable = false)
    @NotEmpty
    private String managerId;

    @Column(name = "location_id ", nullable = false)
    @NotEmpty
    private String locationId;

    @NotEmpty
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "tax_code", length = 13, unique = true, nullable = false)
    private String taxCode;

    @Column(name = "avg_rating_score")
    @Max(value = 5)
    private Float avgRatingScore;

    @Column(name = "status")
    private int status;
}
