package com.teethcare.repository;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findAllByStatusIsNotNull(Pageable pageable);
    List<Feedback> findAllByStatus(Pageable pageable, String status);
    List<Feedback> findAllByBooking(Pageable pageable, Booking booking);
    List<Feedback> findAllByBookingAndStatus(Pageable pageable, Booking booking, String status);
}
