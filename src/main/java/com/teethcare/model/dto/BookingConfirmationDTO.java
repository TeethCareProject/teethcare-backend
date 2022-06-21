package com.teethcare.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingConfirmationDTO {
    private String firstname;
    private String lastname;
    private String fwdLink;
    private String email;
    private int bookingId;
}
