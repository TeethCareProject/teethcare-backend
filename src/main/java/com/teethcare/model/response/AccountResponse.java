package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private int id;
    private String username;
    private int roleId;
    private String roleName;
    private String firstName;
    private String lastName;
    private String avatarImage;
    private String dateOfBirth;
    private String email;
    private String phone;
    private String gender;
    private String status;
}
