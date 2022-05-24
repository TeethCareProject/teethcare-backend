package com.teethcare.config.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String email;
    private String phone;
    private String status;
}
