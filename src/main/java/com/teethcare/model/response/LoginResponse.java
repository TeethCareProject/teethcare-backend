package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String avatarImage;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String gender;
    private String status;
    private String token;
    private String refreshToken;
}
