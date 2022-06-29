package com.teethcare.service;

import com.teethcare.model.dto.StaffCreatingPasswordDTO;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Clinic;

import javax.mail.MessagingException;

public interface EmailService {
    void sendStaffCreatingPasswordEmail(StaffCreatingPasswordDTO staffCreatingPasswordDTO) throws MessagingException;
    void sendBookingConfirmEmail(Booking booking) throws MessagingException;

    void sendClinicApprovementEmail(Clinic clinic) throws MessagingException;

    void sendClinicRejectionEmail(Clinic clinic) throws MessagingException;
    void sendRejectBooking(Booking booking) throws MessagingException;
}
