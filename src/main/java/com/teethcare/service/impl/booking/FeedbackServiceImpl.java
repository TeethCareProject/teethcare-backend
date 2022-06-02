package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Feedback;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.FeedbackRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    private final ClinicService clinicService;

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

    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public void update(Feedback theEntity) {

    }

    @Override
    public Page<Feedback> findAllByClinicID(Pageable pageable, int clinicID, Account account, Integer rating) {
        List<Feedback> feedbacks = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findBookingByClinic(clinicService.findById(clinicID));
        if (!bookings.isEmpty()) {
            if (account == null || !account.getRole().getName().equals(Role.ADMIN)) {
                for (Booking booking : bookings) {
                    if (feedbackRepository.findByBookingIdAndStatus(booking.getId(), Status.Feedback.ACTIVE.name()) != null) {
                        feedbacks.add(feedbackRepository.findByBookingIdAndStatus(booking.getId(), Status.Feedback.ACTIVE.name()));
                    }
                }
            } else {
                for (Booking booking : bookings) {
                    if (feedbackRepository.findByBookingId(booking.getId()) != null) {
                        feedbacks.add(feedbackRepository.findByBookingId(booking.getId()));
                    }
                }
            }
            if (rating != null) {
                Predicate<Feedback> byRating = feedback -> feedback.getRatingScore() == rating;
                feedbacks = feedbacks.stream()
                        .filter(byRating)
                        .collect(Collectors.toList());
            }
        }
        return new PageImpl<>(feedbacks);

    }
}
