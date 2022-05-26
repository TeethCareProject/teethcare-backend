package com.teethcare.model.response;

import java.math.BigDecimal;
import java.util.List;

public class BookingResponse {
    private BigDecimal totalPrice;
    private String createBookingDate;
    private String examinationTime;
    private String text;
    private String appointmentDate;
    private String expireAppointmentDate;
    private String status;
    private int dentistId;
    private String dentistName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String dateOfBirth;
    private String note;
    private String desiredCheckingTime;
    private int customerServiceId;
    private String customerServiceName;
    private List<ServiceOfClinicResponse> serviceList;
}
