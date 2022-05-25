package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomErrorResponse {
    private Timestamp timeStamp;
    private int status;
    private String error;
    private List<String> message;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LoginResponse {
        private String username;
        private String roleName;
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
}
