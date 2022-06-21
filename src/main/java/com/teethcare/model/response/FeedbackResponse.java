package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private int id;
    private int bookingId;
    private String detail;
    private int ratingScore;
    private String status;
    private UserInforResponse patient;
}
