package com.teethcare.service;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.request.BookingFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookingService extends CRUDService<Booking>{
    List<Booking> findAllByCustomerService(CustomerService customerService);

    Booking findBookingById(int id);
    Booking saveBooking(Booking booking);

    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);
    Page<Booking> findAll(String role, int id, BookingFilterRequest filterRequest, Pageable pageable);

    void confirmBookingRequest(int bookingId, boolean isAccepted, CustomerService customerService);
}
