package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class AppointmentRequest {
    @NotNull
    private Integer preBookingId;
    @NotNull
    private Integer serviceId;

    private String description;

    private Timestamp appointmentDate;
    private Timestamp expirationAppointmentDate;

}
