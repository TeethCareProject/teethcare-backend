package com.teethcare.service;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;

import java.util.List;

public interface BookingService extends CRUDService<Booking>{
    List<Booking> findBookingByPatientId(int id);
    Booking saveBooking(Booking booking);
    List<Booking> findBookingByPatientIdAndStatus(int id, String status);
    List<Booking> findAllByCustomerService(CustomerService customerService);
}
