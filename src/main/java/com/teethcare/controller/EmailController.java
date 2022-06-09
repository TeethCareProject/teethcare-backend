package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Email.EMAIL_ENDPOINT)
public class EmailController {

    public final JavaMailSender emailSender;


    @RequestMapping
    public String sendHtmlEmail() throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                +"<img src='http://www.apache.org/images/asf_logo_wide.gif'>";

        message.setContent(htmlMsg, "text/html");

        helper.setTo("conan181101@gmail.com");

        helper.setSubject("Test send HTML email");


        this.emailSender.send(message);

        return "Email Sent!";
    }
}
