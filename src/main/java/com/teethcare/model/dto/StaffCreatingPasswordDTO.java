package com.teethcare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StaffCreatingPasswordDTO {
    String email;
    String username;
    String password;
    String fwdLink;
}
