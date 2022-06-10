package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Email.EMAIL_ENDPOINT)
public class EmailController {
    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<Message> sendStaffCreatingPasswordTestEmail(@RequestBody String emailTo) throws MessagingException {
//        emailService.sendStaffCreatingPasswordEmail(emailTo, "https://www.facebook.com/");
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
