package com.teethcare.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private int id;
    private PatientResponse patient;
    private BigDecimal totalPrice;
    private Long createBookingTime;
    private Long examinationTime;
    private String status;
    private UserInforResponse dentist;
    private String note;
    private Long desiredCheckingTime;
    private UserInforResponse customerService;
    private String description;
    private boolean isRequestChanged;
    private int version;
    private boolean isConfirmed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ServiceOfClinicResponse> services;
    private ClinicSimpleResponse clinic;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FeedbackResponse feedbackResponse;
}
