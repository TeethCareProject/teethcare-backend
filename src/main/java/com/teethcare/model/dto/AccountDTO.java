package com.teethcare.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private String username;
    private String password;
    private int roleId;
    private String firstName;
    private String lastName;
    private boolean gender;
    private String avatarImage;
    private Date dateOfBirth;
    private int status;
    private String phone;
    private String email;
}
