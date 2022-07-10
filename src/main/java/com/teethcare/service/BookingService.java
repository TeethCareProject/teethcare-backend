package com.teethcare.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookingService extends CRUDService<Booking> {
    Booking findBookingById(int id);
    Booking saveBooking(BookingRequest bookingRequest, Account account);
    Booking saveBookingFromAppointment(BookingFromAppointmentRequest bookingFromAppointmentRequest, Account account);

    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);
    Page<Booking> findAll(String role, int id, BookingFilterRequest filterRequest, Pageable pageable);

    boolean confirmBookingRequest(int bookingId, CustomerService customerService, ObjectNode objectNode);
    void rejectBookingRequest(int bookingId);
    void firstlyUpdated(BookingUpdateRequest bookingUpdateRequest, boolean isAllDeleted);

    Order confirmFinalBooking(BookingUpdateRequest bookingUpdateRequest);
    boolean secondlyUpdated(BookingUpdateRequest bookingUpdateRequest, boolean isAllDeleted);

    List<Booking> findBookingByClinic(Clinic clinic);
    boolean checkAvailableTime(CheckAvailableTimeRequest checkAvailableTimeRequest);
    List<Integer> getAvailableTime(GetAvailableTimeRequest getAvailableTimeRequest);
    boolean updateStatus(int bookingId, boolean isCheckin);
}
