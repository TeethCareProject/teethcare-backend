package com.teethcare.service;

import com.teethcare.model.dto.BookingConfirmationDTO;
import com.teethcare.model.dto.StaffCreatingPasswordDTO;
import com.teethcare.model.entity.Booking;

import javax.mail.MessagingException;

public interface EmailService {
    void sendStaffCreatingPasswordEmail(StaffCreatingPasswordDTO staffCreatingPasswordDTO) throws MessagingException;

    void sendBookingConfirmEmail(Booking booking) throws MessagingException;
}
