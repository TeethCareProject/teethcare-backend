package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackByClinicResponse {
    private int id;
    private int bookingID;
    private PatientResponse patientResponse;
    private String detail;
    private int ratingScore;
    private String status;
}
