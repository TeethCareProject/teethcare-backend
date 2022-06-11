package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ReportResponse> reports;
}
