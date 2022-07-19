package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.ForbiddenException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackByClinicResponse;
import com.teethcare.model.response.ReportResponse;
import com.teethcare.repository.FeedbackRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingService bookingService;
    private final FeedbackMapper feedbackMapper;
    private final ReportService reportService;

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback findById(int id) {
        Feedback feedback = feedbackRepository.findFeedbackById(id);
        if (feedback == null) {
            throw new NotFoundException("Feedback " + id + " was not found.");
        }
        return feedback;
    }

    @Override
    public void save(Feedback theEntity) {
        theEntity.setStatus(Status.Feedback.ACTIVE.name());
        feedbackRepository.save(theEntity);
    }

    @Override
    public void delete(int id) {
        Feedback feedback = feedbackRepository.findFeedbackById(id);
        feedback.setStatus(Status.Feedback.INACTIVE.name());
        feedbackRepository.save(feedback);
    }

    @Override
    public void update(Feedback theEntity) {

    }

    @Override
    public Page<FeedbackByClinicResponse> findAllByClinicID(Pageable pageable, Integer clinicID, Account account, Integer rating) {
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks = feedbackRepository.findAllByStatus(Status.Feedback.ACTIVE.name());
        if (!feedbacks.isEmpty()){
            for (Feedback feedback: feedbacks) {
                feedback.setReports(null);
            }
        }
        if (account != null) {
            switch (Role.valueOf(account.getRole().getName())) {
                case CUSTOMER_SERVICE:
                    CustomerService customerService = (CustomerService) account;
                    List<Booking> bookings = bookingService.findBookingByClinic(customerService.getClinic());
                    if (!bookings.isEmpty()) {
                        feedbacks = getAllBookingForCS(bookings);
                    }
                    break;
                case ADMIN:
                    feedbacks = feedbackRepository.findAll();
                    if (!feedbacks.isEmpty()){
                        for (Feedback feedback: feedbacks) {
                            feedback.setReports(null);
                        }
                    }
                    break;
            }
        }
        if (clinicID != null) {
            feedbacks = feedbacks.stream()
                    .filter(feedback -> feedback.getBooking().getClinic().getId().compareTo(clinicID) == 0)
                    .collect(Collectors.toList());
        }
        if (rating != null) {
            feedbacks = feedbacks.stream()
                    .filter(feedback -> feedback.getRatingScore() == rating)
                    .collect(Collectors.toList());
        }
        List<FeedbackByClinicResponse> feedbackByClinicResponses = new ArrayList<>();
        for (Feedback feedback: feedbacks) {
            FeedbackByClinicResponse response = feedbackMapper.mapFeedbackToFeedbackByClinicResponse(feedback);
            if (feedback.getReports() != null) {
                Report report = feedback.getReports().get(0);
                ReportResponse reportResponse = feedbackMapper.mapReportToReportResponse(report);
                response.setReports(reportResponse);
            }
            feedbackByClinicResponses.add(response);
        }
        return PaginationAndSortFactory.convertToPage(feedbackByClinicResponses, pageable);

    }


    @Override
    public Feedback addFeedback(FeedbackRequest feedbackRequest, Account account) {
        int bookingID = feedbackRequest.getBookingId();
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
        feedback.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        Feedback saveFeedback = saveFeedback(feedback);
        return saveFeedback;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        Booking booking = feedback.getBooking();
        Feedback feedback1 = feedbackRepository.findByBookingId(booking.getId());
        if (feedback1 != null) {
            throw new ForbiddenException("You have submitted a feedback for this booking.");
        }
        feedback.setStatus(Status.Feedback.ACTIVE.name());
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback findById(int id, Account account) {
        Feedback feedback = null;
        if (account != null) {
            switch (Role.valueOf(account.getRole().getName())) {
                case CUSTOMER_SERVICE:
                    CustomerService customerService = (CustomerService) account;
                    Clinic clinic = customerService.getClinic();
                    feedback = findById(id);
                    List<Report> reports = reportService.findReportByFeedback(feedback);
                    if (!reports.isEmpty()) {
                        for (Report report : reports) {
                            report.setFeedback(null);
                        }
                        feedback.setReports(reports);
                    }else {
                        feedback.setReports(null);
                    }
                    if (feedback.getBooking().getClinic().getId().compareTo(clinic.getId()) != 0) {
                        feedback = feedbackRepository.findFeedbackByIdAndStatus(id, Status.Feedback.ACTIVE.name());
                        if (feedback == null){
                            throw new NotFoundException("Feedback " + id + " was not found.");
                        }
                        feedback.setReports(null);
                    }
                    break;
                case ADMIN:
                    feedback = findById(id);
                    List<Report> reportList = reportService.findReportByFeedback(feedback);
                    if (!reportList.isEmpty()) {
                        for (Report report : reportList) {
                            report.setFeedback(null);
                        }
                        feedback.setReports(reportList);
                    } else  {
                        feedback.setReports(null);
                    }
                    break;
                default:
                    feedback = feedbackRepository.findFeedbackByIdAndStatus(id, Status.Feedback.ACTIVE.name());
                    if (feedback == null) {
                        throw new NotFoundException("Feedback " + id + " was not found.");
                    }
                    feedback.setReports(null);
                    break;
            }
        }else{
            feedback = feedbackRepository.findFeedbackByIdAndStatus(id, Status.Feedback.ACTIVE.name());
            if (feedback == null) {
                throw new NotFoundException("Feedback " + id + " was not found.");
            }
            feedback.setReports(null);
        }
        return feedback;
    }

    @Override
    public Feedback findByBookingId(int bookingId) {
        return feedbackRepository.findByBookingId(bookingId);
    }

    public List<Feedback> getAllBookingForCS(List<Booking> bookings) {
        List<Feedback> feedbacks = new ArrayList<>();
        for (Booking booking : bookings) {
            Feedback feedback = feedbackRepository.findByBookingId(booking.getId());
            if (feedback != null) {
                feedbacks.add(feedback);
            }
        }
        for (Feedback feedback : feedbacks) {
            List<Report> reports = reportService.findReportByFeedback(feedback);
            if (!reports.isEmpty()) {
                for (Report report : reports) {
                    report.setFeedback(null);
                }
                feedback.setReports(reports);
            }else {
                feedback.setReports(null);
            }
        }
        return feedbacks;
    }



}
