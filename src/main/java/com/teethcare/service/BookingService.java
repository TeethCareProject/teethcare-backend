package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.request.BookingUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookingService extends CRUDService<Booking>{

    Booking findBookingById(int id);
    Booking saveBooking(BookingRequest bookingRequest, Account account);

    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);
    Page<Booking> findAll(String role, int id, BookingFilterRequest filterRequest, Pageable pageable);

    void confirmBookingRequest(int bookingId, boolean isAccepted, CustomerService customerService);
    boolean update(BookingUpdateRequest bookingUpdateRequest);
    boolean updateRequestFromDentist(BookingUpdateRequest bookingUpdateRequest);
    boolean updateStatus(int bookingId);
}
