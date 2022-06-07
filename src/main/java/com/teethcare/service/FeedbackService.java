package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.request.FeedbackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService extends CRUDService<Feedback>{
    Page<Feedback> findAllByClinicID(Pageable pageable, int clinicID, Account account, Integer rating);
    Feedback addFeedback(FeedbackRequest feedbackRequest);
}
