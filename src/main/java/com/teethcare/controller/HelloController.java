package com.teethcare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('CUSTOMER_SERVICE')")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
