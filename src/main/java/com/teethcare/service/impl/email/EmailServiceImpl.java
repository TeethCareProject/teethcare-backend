package com.teethcare.service.impl.email;

import com.teethcare.model.dto.StaffCreatingPasswordDTO;
import com.teethcare.service.EmailService;
import com.teethcare.utils.MailTemplateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

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

}