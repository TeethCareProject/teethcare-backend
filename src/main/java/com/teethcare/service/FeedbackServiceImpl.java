package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Feedback;
import com.teethcare.repository.FeedbackRepository;
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
        return feedbackRepository.getById(id);
    }

    @Override
    public void save(Feedback theEntity) {
        feedbackRepository.save(theEntity);
    }

    @Override
    public void delete(int theId) {
        Feedback feedback = findById(theId);
        if(feedback != null){
            feedback.setStatus(Status.INACTIVE.name());
            feedbackRepository.save(feedback);
        }else{
            throw new NotFoundException("Feedback id "+ theId + " was not found");
        }
    }

    @Override
    public List<Feedback> findAll(Pageable pageable) {
        return feedbackRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public List<Feedback> findByStatus(Pageable pageable, String status) {
        return feedbackRepository.findAllByStatus(pageable, status);
    }
}
