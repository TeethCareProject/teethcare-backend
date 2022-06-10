package com.teethcare.repository;

import com.teethcare.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    List<Booking> findBookingByPatientId(int id, Sort sort);
    List<Booking> findBookingByClinic(Clinic clinic, Sort sort);
    List<Booking> findBookingByDentistId(int id, Sort sort);
    List<Booking> findBookingByDentistIdAndStatus(int id, String status);

    Booking findBookingById(int id);
}
