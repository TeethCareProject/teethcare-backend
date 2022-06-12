package com.teethcare.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AppointmentResponse {
    private int id;
    private PatientResponse patient;
    private BigDecimal totalPrice;
    private String note ;
    private Long appointmentDate;
    private Long expireAppointmentDate;
    private String status;
    private UserInforResponse dentist;
    private UserInforResponse customerService;
    private List<ServiceOfClinicResponse> services;
    private ClinicSimpleResponse clinic;
}
