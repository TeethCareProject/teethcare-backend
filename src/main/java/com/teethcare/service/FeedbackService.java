package com.teethcare.service;

import com.teethcare.model.entity.Feedback;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService extends CRUDService<Feedback>{
    List<Feedback> findAll(Pageable pageable);
    List<Feedback> findByStatus(Pageable pageable, String status);
}
