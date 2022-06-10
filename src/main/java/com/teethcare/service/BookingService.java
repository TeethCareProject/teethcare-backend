package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.request.AppointmentRequest;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookingService extends CRUDService<Booking> {
    List<Booking> findAllByCustomerService(CustomerService customerService);

    Booking findBookingById(int id);

    Booking saveBooking(BookingRequest bookingRequest, Account account);

    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);

    Page<Booking> findAll(String role, int id, BookingFilterRequest filterRequest, Pageable pageable);

    void confirmBookingRequest(int bookingId, boolean isAccepted, CustomerService customerService);

    Booking createAppointment(AppointmentRequest appointmentRequest, Account customerService);
}
