package com.teethcare.repository;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    List<Booking> findBookingByPatientIdAndStatusIsNotNull(int id, Sort sort);

    List<Booking> findBookingByClinicAndStatusIsNotNull(Clinic clinic, Sort sort);

    List<Booking> findBookingByDentistIdAndStatusIsNotNull(int id, Sort sort);

    List<Booking> findBookingByClinic(Clinic id);

    List<Booking> findBookingByStatusAndExaminationTimeAndDentistId(String status, Timestamp examinationTime, int dentistId);

    Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable);

    Booking findBookingById(int id);

    @Query("select b from Booking b " +
            "where b.clinic.id = ?1 and b.desiredCheckingTime between ?2 and ?3 or b.examinationTime between ?4 and ?5")
    List<Booking> findAllBookingByClinicIdAndDesiredCheckingTimeBetweenOrExaminationTimeBetween(int clinicId,
                                                                                                Timestamp lowerDesiredCheckingTime,
                                                                                                Timestamp upperDesiredCheckingTime,
                                                                                                Timestamp lowerExaminationTime,
                                                                                                Timestamp upperExaminationTime);
    Booking findBookingByPreBookingId(int preBookingId);
}
