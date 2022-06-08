package com.teethcare.repository;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    List<Booking> findAllByCustomerService(CustomerService customerService);

    List<Booking> findBookingByStatusNotLike(String status);

    List<Booking> findBookingByPatientId(int id);

    Page<Booking> findBookingByDentistId(int id, Pageable pageable);

    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);

    Booking findBookingById(int id);

    List<Booking> findBookingByClinic(Clinic clinic);

}
