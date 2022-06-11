package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.ForbiddenException;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.repository.FeedbackRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
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
    private final ReportService reportService;

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
    public void delete(int id) {
        Feedback feedback = findById(id);
        feedback.setStatus(Status.Feedback.INACTIVE.name());
        feedbackRepository.save(feedback);
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
            if (account != null) {
                switch (Role.valueOf(account.getRole().getName())) {
                    case CUSTOMER_SERVICE:
                        feedbacks = getAllBookingForCS(bookings, clinicID, account);
                        break;
                    case ADMIN:
                        feedbacks = getAllByBookingForAdmin(bookings);
                        break;
                    default:
                        feedbacks = getAllByBooking(bookings);
                }
            } else {
                feedbacks = getAllByBooking(bookings);
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
        if (!statusBooking.equals(Status.Booking.DONE.name())) {
            throw new BadRequestException("The current booking status cannot send feedback");
        }
        if (booking.getPatient().getId().compareTo(account.getId()) != 0) {
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

    @Override
    public Feedback findById(int id, Account account) {
        Feedback feedback = feedbackRepository.findFeedbackByIdAndStatus(id, Status.Feedback.ACTIVE.name());
        if (account != null) {
            switch (Role.valueOf(account.getRole().getName())) {
                case CUSTOMER_SERVICE:
                    CustomerService customerService = (CustomerService) account;
                    Clinic clinic = customerService.getClinic();
                    feedback = findById(id);
                    if (feedback.getBooking().getClinic().getId().compareTo(clinic.getId()) != 0) {
                        feedback = feedbackRepository.findFeedbackByIdAndStatus(id, Status.Feedback.ACTIVE.name());
                    }
                    break;
                case ADMIN:
                    feedback = findById(id);
                    break;
            }
        }
        return feedback;
    }

    public List<Feedback> getAllByBooking(List<Booking> bookings) {
        List<Feedback> feedbacks = new ArrayList<>();
        for (Booking booking : bookings) {
            if (feedbackRepository.findByBookingIdAndStatus(booking.getId(), Status.Feedback.ACTIVE.name()) != null) {
                feedbacks.add(feedbackRepository.findByBookingIdAndStatus(booking.getId(), Status.Feedback.ACTIVE.name()));
            }
        }
        for (Feedback feedback : feedbacks) {
            feedback.setReports(null);
        }
        return feedbacks;
    }

    public List<Feedback> getAllByBookingForAdmin(List<Booking> bookings) {
        List<Feedback> feedbacks = new ArrayList<>();
        for (Booking booking : bookings) {
            if (feedbackRepository.findByBookingId(booking.getId()) != null) {
                feedbacks.add(feedbackRepository.findByBookingId(booking.getId()));
            }
        }
        for (Feedback feedback : feedbacks) {
            feedback.setReports(null);
        }
        return feedbacks;
    }

    public List<Feedback> getAllBookingForCS(List<Booking> bookings, int clinicId, Account account) {
        CustomerService customerService = (CustomerService) account;
        List<Feedback> feedbacks = new ArrayList<>();
        if (customerService.getClinic().getId() == clinicId) {
            feedbacks = getAllByBookingForAdmin(bookings);
            for (Feedback feebdback : feedbacks) {
                List<Report> reports = new ArrayList<>();
                reports.add(reportService.findReportByFeedback(feebdback).get(0));
                for (Report report : reports) {
                    report.setFeedback(null);
                }
                feebdback.setReports(reports);
            }
        } else {
            feedbacks = getAllByBooking(bookings);
        }
        return feedbacks;
    }

}
