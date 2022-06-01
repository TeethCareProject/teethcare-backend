package com.teethcare.model.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private String password;
    private int roleId;
    private String fistName;
    private String lastName;
    private boolean gender;
    private String avatarImage;
    private Date dateOfBirth;
    private int status;
}
