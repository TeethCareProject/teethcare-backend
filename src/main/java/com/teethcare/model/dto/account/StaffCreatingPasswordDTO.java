package com.teethcare.model.dto.account;

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
