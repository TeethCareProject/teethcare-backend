package com.teethcare.controller;

import com.teethcare.common.Message;
import com.teethcare.model.request.ForgotPasswordRequest;
import com.teethcare.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    public ResponseEntity<Message> createKey(@RequestBody String username) {
        forgotPasswordService.addKey(username);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Message> changePassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        forgotPasswordService.changePassword(forgotPasswordRequest);
        return new ResponseEntity<>(Message.SUCCESS_FUNCTION, HttpStatus.OK);
    }
}
