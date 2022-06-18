package com.teethcare.repository;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Feedback findByBookingId(int bookingID);

    Feedback findByBookingIdAndStatus(int bookingId, String status);
    Feedback findFeedbackById(int id);
    Feedback save(Feedback feedback);
    Feedback findFeedbackByIdAndStatus(int id, String status);
    Feedback findFeedbackByIdAndBooking_Clinic(int id, Clinic clinic);

    List<Feedback> findAllByStatus(String status);

}
