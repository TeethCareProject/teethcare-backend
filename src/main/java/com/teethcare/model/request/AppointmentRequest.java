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
    private Integer serviceId;
    private String note;
    private Long appointmentDate;
    private Long expirationAppointmentDate;

}
