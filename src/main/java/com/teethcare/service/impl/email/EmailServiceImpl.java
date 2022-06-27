package com.teethcare.service.impl.email;

import com.teethcare.model.dto.BookingConfirmationDTO;
import com.teethcare.model.dto.StaffCreatingPasswordDTO;
import com.teethcare.model.entity.Booking;
import com.teethcare.service.EmailService;
import com.teethcare.utils.MailTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${front.end.origin}")
    private String homepageUrl;

    public final JavaMailSender emailSender;

    @Override
    public void sendStaffCreatingPasswordEmail(StaffCreatingPasswordDTO staffCreatingPasswordDTO) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = MailTemplateUtils.getStaffCreatingPasswordEmail(staffCreatingPasswordDTO.getUsername(), staffCreatingPasswordDTO.getPassword(), staffCreatingPasswordDTO.getFwdLink());

        message.setContent(htmlMsg, "text/html");

        helper.setTo(staffCreatingPasswordDTO.getEmail());

        helper.setSubject("[TEETHCARE] YOU ARE ADDED INTO NEW CLINIC");

        this.emailSender.send(message);
    }

    @Override
    public void sendBookingConfirmEmail(Booking booking) throws MessagingException {
        BookingConfirmationDTO bookingConfirmationDTO =
                BookingConfirmationDTO.builder()
                        .firstname(booking.getPatient().getFirstName())
                        .lastname(booking.getPatient().getFirstName())
                        .email(booking.getPatient().getEmail())
                        .bookingId(booking.getId())
                        .fwdLink(homepageUrl + "confirmBooking/" + booking.getId() + "?version=" + booking.getVersion())
                        .build();
        log.info("Forward link: " + homepageUrl + "confirmBooking/" + booking.getId() + "?version=" + booking.getVersion());
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = MailTemplateUtils.getBookingConfirmation(bookingConfirmationDTO);

        message.setContent(htmlMsg, "text/html");

        helper.setTo(bookingConfirmationDTO.getEmail());
        helper.setSubject("[TEETHCARE] YOUR BOOKING IS UPDATED!");

        this.emailSender.send(message);
    }

    @Override
    public void sendRejectBooking(Booking booking) throws MessagingException {
        BookingConfirmationDTO bookingConfirmationDTO =
                BookingConfirmationDTO.builder()
                        .firstname(booking.getPatient().getFirstName())
                        .lastname(booking.getPatient().getFirstName())
                        .email(booking.getPatient().getEmail())
                        .bookingId(booking.getId())
                        .content(booking.getRejectedNote())
                        .clinicName(booking.getClinic().getName())
                        .build();
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = MailTemplateUtils.getBookingRejection(bookingConfirmationDTO);

        message.setContent(htmlMsg, "text/html");

        helper.setTo(bookingConfirmationDTO.getEmail());
        helper.setSubject("[TEETHCARE] YOUR BOOKING IS REJECTD!");

        this.emailSender.send(message);
    }
}