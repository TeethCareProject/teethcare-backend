package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentUpdateRequest {
    private String note;
    private Long appointmentDate;
    private Long expirationAppointmentDate;
}
