package com.teethcare.repository;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.ServiceOfClinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingByPatientId(int id);

    List<Booking> findBookingByPatientIdAndStatus(int id, String status);
    List<Booking> findAllByCustomerService(CustomerService customerService);
    List<Booking> findBookingByClinic(Clinic clinic);
}
