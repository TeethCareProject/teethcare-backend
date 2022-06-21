package com.teethcare.model.dto;

import com.teethcare.model.entity.Dentist;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.model.response.PatientResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
