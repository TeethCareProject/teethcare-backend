package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.model.response.PatientBookingResponse;
import com.teethcare.service.*;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private final CSService CSService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ClinicService clinicService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<PatientBookingResponse> bookingService(@Valid @RequestBody BookingRequest bookingRequest,
                                                                 @RequestHeader(AUTHORIZATION) String token) {

        token = token.substring("Bearer ".length());
        Account account = jwtTokenUtil.getAccountFromJwt(token);

        Booking booking = bookingService.saveBooking(bookingRequest, account);
        PatientBookingResponse patientBookingResponse = bookingMapper.mapBookingToPatientBookingResponse(booking);
        patientBookingResponse.setDesiredCheckingTime(booking.getDesiredCheckingTime().getTime());

        ServiceOfClinic service = serviceOfClinicService.findById(bookingRequest.getServiceId());
        patientBookingResponse.setServiceName(service.getName());

        return new ResponseEntity<>(patientBookingResponse, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).PATIENT, " +
            "T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<Page<BookingResponse>> getAll(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                        @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction,
                                                        BookingFilterRequest requestFilter,
                                                        @RequestHeader(AUTHORIZATION) String header) {
        String token = header.substring("Bearer ".length());
        Account account = jwtTokenUtil.getAccountFromJwt(token);

        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);

        Page<Booking> bookingPage = bookingService.findAll(account.getRole().getName(), account.getId(), requestFilter, pageable);

        Page<BookingResponse> bookingResponsePage = bookingPage.map(bookingMapper::mapBookingToBookingResponse);

        return new ResponseEntity<>(bookingResponsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable("id") int id) {
        Booking booking = bookingService.findBookingById(id);
        BookingResponse bookingResponse = bookingMapper.mapBookingToBookingResponse(booking);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> isAccepted(@RequestParam(value = "isAccepted") boolean isAccepted,
                                                      @PathVariable(value = "id") int bookingId,
                                                      @RequestHeader(AUTHORIZATION) String header) {
        String token = header.substring("Bearer ".length());
        Account account = jwtTokenUtil.getAccountFromJwt(token);

        CustomerService customerService = CSService.findById(account.getId());

        bookingService.confirmBookingRequest(bookingId, isAccepted, customerService);

        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }
}
