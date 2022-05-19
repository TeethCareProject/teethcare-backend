package com.teethcare.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenRequest {
    private String refreshToken;
}
