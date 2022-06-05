package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientRegisterRequest {
    @NotBlank
    @Length(max = 72)
    private String username;

    @NotBlank
    @Length(max = 72)
    private String password;

    @NotBlank
    @Length(max = 72)
    private String confirmPassword;

    @NotBlank
    @Length(max = 40)
    private String firstName;

    @NotBlank
    @Length(max = 10)
    private String lastName;

    @NotBlank
    @Length(max = 10)
    private String gender;

    @Email
    @NotBlank
    @Length(max = 320)
    private String email;

    @NotBlank
    @Pattern(regexp = "(0)+(\\d){9}")
    private String phoneNumber;
}
