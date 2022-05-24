package com.teethcare.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
