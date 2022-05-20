package com.teethcare.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenRequest {
    @NotEmpty
    private String refreshToken;
}
