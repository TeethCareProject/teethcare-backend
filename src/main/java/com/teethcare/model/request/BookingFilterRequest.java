package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingFilterRequest {
    Integer bookingId;
    String patientPhone;
    String patientName;
    Integer customerServiceId;
    Integer dentistId;
    String clinicName;
}
