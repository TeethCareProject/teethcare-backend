package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackByClinicResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService extends CRUDService<Feedback> {
    Page<FeedbackByClinicResponse> findAllByClinicID(Pageable pageable, Integer clinicID, Account account, Integer rating);

    Feedback addFeedback(FeedbackRequest feedbackRequest, Account account);

    Feedback saveFeedback(Feedback feedback);

    Feedback findById(int id, Account account);

    Feedback findByBookingId(int bookingId);

}
