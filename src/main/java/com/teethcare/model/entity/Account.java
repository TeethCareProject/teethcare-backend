package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
@Entity(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name = "username", length = 72, nullable = false)
    private String username;

    @Column(name = "password", length = 72, nullable = false)
    @NotEmpty
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role roleId;

    @NotBlank
    @Column(name = "first_name", length = 40, nullable = false)
    private String fistName;

    @NotBlank
    @Column(name = "last_name", length = 10, nullable = false)
    private String lastName;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "avatar_image")
    private String avatarImage;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "status")
    private boolean status;
}
