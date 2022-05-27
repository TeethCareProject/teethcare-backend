package com.teethcare.repository;

import com.teethcare.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingByPatientId(int id);
    List<Booking> findBookingByPatientIdAndStatus(int id, String status);
}
