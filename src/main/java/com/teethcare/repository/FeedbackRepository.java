package com.teethcare.repository;

import com.teethcare.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Feedback findByBookingId(int bookingID);
    Feedback findByBookingIdAndStatus(int bookingId, String status);
}
