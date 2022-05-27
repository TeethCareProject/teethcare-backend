package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.BookingMapper;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.service.BookingService;
import com.teethcare.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Booking.BOOKING_ENDPOINT)
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final PatientService patientService;
}
