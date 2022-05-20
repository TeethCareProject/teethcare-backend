package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String username;
    @JsonIgnore
    private String password;
    private String roles;
    private String firstName;
    private String lastName;
    private int  gender;
    private String avatarImage;
    private Date dateOfBirth;
    private int status;
    private String token;
    private String refreshToken;
}
