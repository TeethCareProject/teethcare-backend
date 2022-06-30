package com.teethcare.model.dto.booking;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingConfirmationDTO {
    @Value(value = "${front_end_origin}")
    private String homepageUrl;
    private String firstname;
    private String lastname;
    private String fwdLink;
    private String email;
    private int bookingId;
    private String content;
    private String clinicName;
}
