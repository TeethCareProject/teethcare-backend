package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Integer id;
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer roleId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roleName;
    private String firstName;
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatarImage;
    private Long dateOfBirth;
    private String email;
    private String phone;
    private String gender;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
}
