package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.AppointmentRequest;
import com.teethcare.model.response.PatientBookingResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.BookingService;
import com.teethcare.service.ServiceOfClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Appointment.APPOINTMENT_ENDPOINT)
//@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
public class AppointmentController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;
    private final BookingMapper bookingMapper;
    private final ServiceOfClinicService serviceOfClinicService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<PatientBookingResponse> addAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest,
                                           @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Account customerService = accountService.getAccountByUsername(username);
        Booking booking = bookingService.createAppointment(appointmentRequest, customerService);
        PatientBookingResponse patientBookingResponse = bookingMapper.mapBookingToPatientBookingResponse(booking);
        patientBookingResponse.setDesiredCheckingTime(booking.getDesiredCheckingTime().getTime());

        ServiceOfClinic service = serviceOfClinicService.findById(appointmentRequest.getServiceId());
        patientBookingResponse.setServiceName(service.getName());

        return new ResponseEntity<>(patientBookingResponse, HttpStatus.OK);
    }

}
