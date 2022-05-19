package com.teethcare.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_id",
        discriminatorType = DiscriminatorType.INTEGER)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "username", length = 72, nullable = false)
    private String username;

    @Column(name = "password", length = 72, nullable = false)
    @NotEmpty
    private String password;

    @Column(name = "role_id", nullable = false, insertable = false, updatable = false)
    @NotEmpty
    private int roleId;

    @NotEmpty
    @Column(name = "first_name", length = 40, nullable = false)
    private String fistName;

    @NotEmpty
    @Column(name = "last_name", length = 10, nullable = false)
    private String lastName;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "avatar_image")
    private String avatarImage;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @NotEmpty
    private boolean status;


}
