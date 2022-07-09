package com.teethcare.service;

import com.teethcare.model.dto.account.StaffCreatingPasswordDTO;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Order;

import javax.mail.MessagingException;

public interface EmailService {
    void sendStaffCreatingPasswordEmail(StaffCreatingPasswordDTO staffCreatingPasswordDTO) throws MessagingException;
    void sendBookingConfirmEmail(Booking booking) throws MessagingException;

    void sendClinicApprovementEmail(Clinic clinic) throws MessagingException;

    void sendClinicRejectionEmail(Clinic clinic) throws MessagingException;
    void sendRejectBooking(Booking booking) throws MessagingException;
    void sendOrderDetail(Order order) throws MessagingException;
}
