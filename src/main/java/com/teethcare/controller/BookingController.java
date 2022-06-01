package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.model.response.PatientBookingResponse;
import com.teethcare.service.BookingService;
import com.teethcare.service.CSService;
import com.teethcare.service.PatientService;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.specification.SpecificationFactory;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private final CSService CSService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<PatientBookingResponse> bookingService(@Valid @RequestBody BookingRequest bookingRequest,
                                                                 @RequestHeader(AUTHORIZATION) String token){
        Booking bookingTmp = bookingMapper.mapBookingRequestToBooking(bookingRequest);
        token = token.substring("Bearer ".length());
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
        bookingTmp.setServices(serviceOfClinicList);

        //get patient by id
        Patient patient = patientService.findById(account.getId());

        //set patient to booking
        bookingTmp.setPatient(patient);
        bookingTmp.setStatus(Status.PENDING.name());
        PatientBookingResponse patientBookingResponse = null;

        //map booking to booking response
        if (patient != null && !serviceOfClinicList.isEmpty() && serviceOfClinicService.findById(serviceID) != null){
            Booking booking = bookingService.saveBooking(bookingTmp);
            patientBookingResponse = bookingMapper.mapBookingToPatientBookingResponse(booking);
            patientBookingResponse.setServiceName(serviceOfClinicService.findById(serviceID).getName());
            patientBookingResponse.setDesiredCheckingTime(booking.getDesiredCheckingTime().getTime());
        }
        return new ResponseEntity<>(patientBookingResponse, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).PATIENT, " +
            "T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<Page<BookingResponse>> getAll(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                        @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction,
                                                        @RequestParam(value = "search", required = false) String search,
                                                        @RequestParam(value = "clinicName", required = false) String clinicName,
                                                        @RequestParam(value = "bookingId", required = false, defaultValue = "-1") int bookingId,
                                                        @RequestHeader(AUTHORIZATION) String header) {
        String token = header.substring("Bearer ".length());
        Account account = jwtTokenUtil.getAccountFromJwt(token);

        System.out.println("In booking controller");
        SpecificationFactory bookingSpecificationFactory = new SpecificationFactory();
        Specification<Booking> specification = bookingSpecificationFactory.getSpecification(search);
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);

        Page<Booking> bookingPage  = bookingService.findAll(account.getRole().getName(), account.getId(), clinicName, bookingId, specification, pageable);
        System.out.println("In booking controller, after find");
        Page<BookingResponse> bookingResponsePage = bookingPage.map(bookingMapper::mapBookingToBookingResponse);

        return new ResponseEntity<>(bookingResponsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable("id") int id) {
        Booking booking = bookingService.findBookingById(id);
        BookingResponse bookingResponse = bookingMapper.mapBookingToBookingResponse(booking);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @PutMapping("/accept")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<MessageResponse> isAccepted(@RequestParam(value = "isAccepted") boolean isAccepted,
                                                      @RequestParam(value = "bookingId") int bookingId,
                                                      @RequestHeader(AUTHORIZATION) String header) {
        String token = header.substring("Bearer ".length());
        Account account = jwtTokenUtil.getAccountFromJwt(token);

        Booking booking = bookingService.findBookingById(bookingId);
        CustomerService customerService = CSService.findById(account.getId());

        booking.setId(bookingId);
        if (isAccepted) {
            booking.setStatus(Status.REQUEST.name());
        } else {
            booking.setStatus(Status.REJECTED.name());
        }
        booking.setCustomerService(customerService);

        bookingService.save(booking);

        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }
}
