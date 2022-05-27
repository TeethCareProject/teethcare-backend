package com.teethcare.model.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private int serviceId;

    private int patientId;

    private String description;

    private Timestamp desiredCheckingTime;

}
