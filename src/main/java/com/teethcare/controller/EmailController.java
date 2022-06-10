package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Email.EMAIL_ENDPOINT)
public class EmailController {

}
