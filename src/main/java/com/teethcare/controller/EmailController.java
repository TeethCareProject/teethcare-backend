package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Message> sendHtmlEmail() throws MessagingException, IOException {
        emailService.sendStaffCreatingPasswordEmail("conan181101@gmail.com", "https://www.facebook.com/");
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
