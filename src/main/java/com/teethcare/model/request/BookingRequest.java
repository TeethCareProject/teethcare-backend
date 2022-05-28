package com.teethcare.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotEmpty
    private int serviceId;

    @NotEmpty
    private int patientId;

    private String description;

    @NotEmpty
    private Timestamp desiredCheckingTime;

}
