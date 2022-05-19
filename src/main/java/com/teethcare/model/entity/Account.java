package com.teethcare.model.entity;

import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    private Integer id;

    @NotEmpty
    @Column(name = "username", length = 72, nullable = false)
    private String username;

    @Column(name = "password", length = 72, nullable = false)
    @NotEmpty
    private String password;

    @Column(name = "role_id", nullable = false, insertable = false, updatable = false)
    private int roleId;

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
