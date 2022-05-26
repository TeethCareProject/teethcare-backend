package com.teethcare.controller;

import com.teethcare.model.response.BookingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllUnsignedBooking() {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        return new ResponseEntity<>(bookingResponseList, HttpStatus.OK);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<BookingResponse>> getHistoryBooking(@PathVariable int id) {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        return new ResponseEntity<>(bookingResponseList, HttpStatus.OK);
    }

    @GetMapping("/customer-services/{id}")
    public ResponseEntity<List<BookingResponse>> getAllBooking(@PathVariable int id) {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        return new ResponseEntity<>(bookingResponseList, HttpStatus.OK);
    }
}
