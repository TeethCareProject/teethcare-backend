package com.teethcare.repository;

import com.teethcare.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    List<Booking> findBookingByPatientIdAndStatus(int id, String status);
    List<Booking> findAllByCustomerService(CustomerService customerService);
    List<Booking> findBookingByStatusNotLike(String status, Pageable pageable);
    List<Booking> findBookingByPatientId(int id, Pageable pageable);

    Page<Booking> findBookingByIdAndPatientId(int bookingId, int patientId, Pageable pageable);
    Page<Booking> findBookingByIdAndPatientIdAndDentistClinicNameLike(int bookingId, int patientId, String clinicName, Pageable pageable);
    Page<Booking> findBookingByPatientIdAndClinicNameLike(int patient_id, String clinic_name, Pageable pageable);
//    Page<Booking> findBookingByPatientId(int id, Pageable pageable);
    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);

    Booking findBookingById(int id);
}
