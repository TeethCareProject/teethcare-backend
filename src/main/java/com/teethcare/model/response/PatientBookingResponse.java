package com.teethcare.model.response;

import com.teethcare.model.entity.ServiceOfClinic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientBookingResponse {
    private int bookingID;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String serviceName;
    private String description;
    private long desiredCheckingTime;
    private ClinicSimpleResponse clinic;
}
