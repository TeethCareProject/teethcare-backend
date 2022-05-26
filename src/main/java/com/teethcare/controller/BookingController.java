package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.exception.IdInvalidException;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@RestController
//@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Booking.BOOKING_ENDPOINT)
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllUnsignedBooking() {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        return new ResponseEntity<>(bookingResponseList, HttpStatus.OK);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<Booking>> getHistoryBooking(@PathVariable String id) {
        int theId = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new IdInvalidException("Id " + id + " invalid");
        }
        theId = Integer.parseInt(id);

        List<Booking> bookingList = bookingService.findBookingByPatientId(theId);
//        List<AccountResponse> staffResponseList = new ArrayList<>();

//        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(theID, "ACTIVE");
//        List<CustomerService> customerServiceList = csService.findByClinicIdAndStatus(theID, "ACTIVE");

//        staffList.addAll(dentistList);
//        staffList.addAll(customerServiceList);
//
//        staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);

        if (bookingList == null || bookingList.size() == 0) {
            throw new IdNotFoundException("With id "+ id + ", the list of booking could not be found.");
        }
        return new ResponseEntity<>(bookingList, HttpStatus.OK);
    }

    @GetMapping("/customer-services/{id}")
    public ResponseEntity<List<BookingResponse>> getAllBooking(@PathVariable int id) {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        return new ResponseEntity<>(bookingResponseList, HttpStatus.OK);
    }
}
