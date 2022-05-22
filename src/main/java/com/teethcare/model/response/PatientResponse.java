package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private int id;
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String gender;
    private String avatarImage;
    private Date dateOfBirth;
    private String status;
}
