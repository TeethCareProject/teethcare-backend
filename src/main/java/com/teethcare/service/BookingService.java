package com.teethcare.service;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookingService extends CRUDService<Booking>{
    List<Booking> findBookingByPatientId(int id);
    Booking saveBooking(Booking booking);
    List<Booking> findAllByCustomerService(CustomerService customerService);



    List<Booking> findBookingByPatientIdAndStatus(int id, String status);
    //    List<Booking> findBookingByPatientId(int id, Pageable pageable);
    List<Booking> findBookingByDentistId(int id, Pageable pageable);
    List<Booking> findBookingByStatusNotLike(String status);
    List<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);
    Page<Booking> findBookingByPatientId(int id, Pageable pageable);
    Page<Booking> findBookingByDentistClinicNameAndPatientId(String clinicName, String patientId, Pageable pageable);

    Page<Booking> findAll(String role, int id, String clinicName, Specification<Booking> bookingSpecification, Pageable pageable);
}
