package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private BookingResponse bookingResponse;
    private String detail;
    private int ratingScore;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReportResponse reports;
}
