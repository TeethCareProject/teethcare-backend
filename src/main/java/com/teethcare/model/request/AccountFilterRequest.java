package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountFilterRequest {
    String id;
    String fullName;
    String username;
    String role;
    String email;
    String phone;
    String status;
}
