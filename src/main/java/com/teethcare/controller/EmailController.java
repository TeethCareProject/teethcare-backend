package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Email.EMAIL_ENDPOINT)
public class EmailController {

    //    public final JavaMailSender emailSender;
    private final EmailService emailService;

    @GetMapping
    public String sendHtmlEmail() throws MessagingException, IOException {
        emailService.sendmail();
        return "h";

    }
}
