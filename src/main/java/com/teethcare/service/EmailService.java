package com.teethcare.service;

import com.teethcare.model.dto.BookingConfirmationDTO;
import com.teethcare.model.dto.StaffCreatingPasswordDTO;

import javax.mail.MessagingException;

public interface EmailService {
    void sendStaffCreatingPasswordEmail(StaffCreatingPasswordDTO staffCreatingPasswordDTO) throws MessagingException;

    void sendBookingConfirmEmail(BookingConfirmationDTO bookingResponse) throws MessagingException;
}
