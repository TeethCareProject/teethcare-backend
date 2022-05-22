package com.teethcare.model.response;

import com.teethcare.model.entity.Clinic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {
    private int id;
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String status;
    private Clinic clinic;
}
