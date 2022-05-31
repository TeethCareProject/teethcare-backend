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
//    private BigDecimal totalPrice;
//    private log createBookingDate;
//    private String examinationTime;
//    private String text;
//    private String appointmentDate;
//    private String expireAppointmentDate;
//    private String status;
//    private int dentistId;
//    private String dentistName;
//    private String firstName;
//    private String lastName;
//    private String phone;
//    private String email;
//    private long dateOfBirth;
//    private String note;
//    private String desiredCheckingTime;
//    private int customerServiceId;
//    private String customerServiceName;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<ServiceOfClinicResponse> serviceList;

    private int id;
    private BigDecimal totalPrice;
    private long createBookingDate;
    private long examinationTime;
    private String text;
    private long appointmentDate;
    private long expireAppointmentDate;
    private String status;
    private int dentistId;
    private String dentistName;
    private String note;
    private long desiredCheckingTime;
    private int customerServiceId;
    private String customerServiceName;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ServiceOfClinicResponse> services;
}
