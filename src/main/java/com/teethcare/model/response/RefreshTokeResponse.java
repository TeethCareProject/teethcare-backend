package com.teethcare.model.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokeResponse {
    private String token;
    private String refreshToken;
}
