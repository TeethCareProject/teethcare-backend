package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.ForbiddenException;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.repository.FeedbackRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.FeedbackService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingService bookingService;
    private final ClinicService clinicService;
    private final FeedbackMapper feedbackMapper;

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback findById(int id) {
        return feedbackRepository.findFeedbackById(id);
    }

    @Override
    public void save(Feedback theEntity) {
        theEntity.setStatus(Status.Feedback.ACTIVE.name());
        feedbackRepository.save(theEntity);
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
        Clinic clinic = clinicService.findById(clinicID);
        List<Booking> bookings = bookingService.findBookingByClinic(clinic);
        if (!bookings.isEmpty()) {
            if (account == null || !account.getRole().getName().equals(Role.ADMIN.name())) {
                feedbacks = getAllByBooking(bookings);
            } else {
                feedbacks = getAllByBookingForAdmin(bookings);
            }
            if (rating != null) {
                feedbacks = feedbacks.stream()
                        .filter(feedback -> feedback.getRatingScore() == rating)
                        .collect(Collectors.toList());
            }
        }
        return PaginationAndSortFactory.convertToPage(feedbacks, pageable);

    }

    @Override
    public Feedback addFeedback(FeedbackRequest feedbackRequest, Account account) {
        int bookingID = feedbackRequest.getBookingID();
        Booking booking = bookingService.findBookingById(bookingID);
        String statusBooking = booking.getStatus();
        if (!statusBooking.equals(Status.Booking.DONE.name())){
            throw new BadRequestException("The current booking status cannot send feedback");
        }
        if (booking.getPatient().getId().compareTo(account.getId()) != 0){
            throw new ForbiddenException("You can not send feedback for this booking");
        }
        Feedback feedback = feedbackMapper.mapFeedbackRequestToFeedback(feedbackRequest);
        feedback.setBooking(booking);
        Feedback saveFeedback = saveFeedback(feedback);
        return saveFeedback;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        feedback.setStatus(Status.Feedback.ACTIVE.name());
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllByBooking(List<Booking> bookings){
        List<Feedback> feedbacks = new ArrayList<>();
        for (Booking booking : bookings) {
            if (feedbackRepository.findByBookingIdAndStatus(booking.getId(), Status.Feedback.ACTIVE.name()) != null) {
                feedbacks.add(feedbackRepository.findByBookingIdAndStatus(booking.getId(), Status.Feedback.ACTIVE.name()));
            }
        }
        return feedbacks;
    }

    public List<Feedback> getAllByBookingForAdmin(List<Booking> bookings){
        List<Feedback> feedbacks = new ArrayList<>();
        for (Booking booking : bookings) {
            if (feedbackRepository.findByBookingId(booking.getId()) != null) {
                feedbacks.add(feedbackRepository.findByBookingId(booking.getId()));
            }
        }
        return feedbacks;
    }

}
