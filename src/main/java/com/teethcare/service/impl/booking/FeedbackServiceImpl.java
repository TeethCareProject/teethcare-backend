package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Feedback;
import com.teethcare.repository.FeedbackRepository;
import com.teethcare.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback findById(int id) {
        Feedback feedback = feedbackRepository.getById(id);
        return feedback;
    }

    @Override
    public void save(Feedback theEntity) {
        feedbackRepository.save(theEntity);
    }

    @Override
    public void delete(int theId) {
        Feedback feedback = findById(theId);
        feedback.setStatus(Status.Feedback.INACTIVE.name());
        feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> findAll(Pageable pageable) {
        return feedbackRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public List<Feedback> findByStatus(Pageable pageable, String status) {
        return feedbackRepository.findAllByStatus(pageable, status);
    }

    @Override
    public List<Feedback> findAllByBooking(Pageable pageable, Booking booking) {
        return feedbackRepository.findAllByBooking(pageable, booking);
    }

    @Override
    public List<Feedback> findAllByBookingAndStatus(Pageable pageable, Booking booking, String status) {
        return feedbackRepository.findAllByBookingAndStatus(pageable, booking, status);
    }

}
