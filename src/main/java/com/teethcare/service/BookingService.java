package com.teethcare.service;

import com.teethcare.model.entity.Booking;

import java.util.List;

public interface BookingService extends CRUDService<Booking>{
    List<Booking> findBookingByPatientId(int id);
}
