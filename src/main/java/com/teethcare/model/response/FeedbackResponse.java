package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private int id;
    private int bookingID;
    private String firstName;
    private String lastName;
    private String detail;
    private int ratingScore;
    private String status;
    private ClinicInfoResponse clinicInfoResponse;
}
