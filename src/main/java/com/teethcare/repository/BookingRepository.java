package com.teethcare.repository;

import com.teethcare.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    List<Booking> findAllByCustomerService(CustomerService customerService);
    List<Booking> findBookingByPatientId(int id);
    List<Booking> findBookingByClinic(Clinic clinic);
    List<Booking> findBookingByDentistId(int id);

    Booking findBookingById(int id);

}
