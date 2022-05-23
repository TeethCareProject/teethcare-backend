package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountResponse {
    @JsonProperty
    private String id;
    @JsonProperty
    private String username;
    @JsonProperty
    private int roleId;
    @JsonProperty
    private String fistName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String avatarImage;
    @JsonProperty
    private String phone;
    @JsonProperty
    private String email;
    @JsonProperty
    private String gender;
    @JsonProperty
    private String dateOfBirth;
    @JsonProperty
    private String status;
}
