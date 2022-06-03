package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.PatientBookingResponse;
import com.teethcare.service.BookingService;
import com.teethcare.service.PatientService;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Booking.BOOKING_ENDPOINT)
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final PatientService patientService;
    private final ServiceOfClinicService serviceOfClinicService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<PatientBookingResponse> bookingService(@Valid @RequestBody BookingRequest bookingRequest,
                                                                 HttpServletRequest request) {
        Booking bookingTmp = bookingMapper.mapBookingRequestToBooking(bookingRequest);
        String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
        Account account = jwtTokenUtil.getAccountFromJwt(token);
        //get milisecond
        long milisecond = bookingRequest.getDesiredCheckingTime();

        Timestamp desiredCheckingTime = ConvertUtils.getTimestamp(milisecond);
        bookingTmp.setDesiredCheckingTime(desiredCheckingTime);

        //get service by id
        int serviceID = bookingRequest.getServiceId();
        List<ServiceOfClinic> serviceOfClinicList = new ArrayList<>();
        serviceOfClinicList.add(serviceOfClinicService.findById(serviceID));

        //set service to booking
        bookingTmp.setServiceOfClinics(serviceOfClinicList);

        //get patient by id
        Patient patient = patientService.findById(account.getId());

        //set patient to booking
        bookingTmp.setPatient(patient);
        bookingTmp.setStatus(Status.Booking.PENDING.name());
        PatientBookingResponse patientBookingResponse = null;

        //map booking to booking response
        if (patient != null && !serviceOfClinicList.isEmpty() && serviceOfClinicService.findById(serviceID) != null) {
            Booking booking = bookingService.saveBooking(bookingTmp);
            patientBookingResponse = bookingMapper.mapBookingToPatientBookingResponse(booking);
            patientBookingResponse.setServiceName(serviceOfClinicService.findById(serviceID).getName());
            patientBookingResponse.setDesiredCheckingTime(booking.getDesiredCheckingTime().getTime());
        }
        return new ResponseEntity<>(patientBookingResponse, HttpStatus.OK);
    }
}
