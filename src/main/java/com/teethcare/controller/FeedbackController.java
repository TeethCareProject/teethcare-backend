package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackResponse;
import com.teethcare.service.BookingService;
import com.teethcare.service.CSService;
import com.teethcare.service.FeedbackService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = EndpointConstant.Feedback.FEEDBACK_ENDPOINT)
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;
    private final BookingService bookingService;
    private final CSService csService;

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAll(@RequestParam(name = "status", required = false) String status,
                                                         @RequestParam(name = "clinicID", required = false) Integer clinicID,
                                                         @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                         @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                         @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        List<Feedback> list = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        if (clinicID != null) {
            List<CustomerService> customerServiceList = csService.findByClinicId(clinicID);
            for (CustomerService cs : customerServiceList) {
                bookings.addAll(bookingService.findAllByCustomerService(cs));
            }
            for (Booking booking : bookings) {
                if (status != null) {
                    list.addAll(feedbackService.findAllByBookingAndStatus(pageable, booking, status));
                } else {
                    list.addAll(feedbackService.findAllByBooking(pageable, booking));
                }
            }
        } else if (status != null) {
            System.out.println("Hellu");
            list = feedbackService.findByStatus(pageable, status);
        } else {
            list = feedbackService.findAll(pageable);
        }

        List<FeedbackResponse> feedbackResponses = feedbackMapper.mapFeedbackListToFeedbackResponseList(list);

        return new ResponseEntity(feedbackResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getById(@PathVariable("id") String id) {
        int theId = ConvertUtils.covertID(id);
        Feedback feedback = feedbackService.findById(theId);
        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theId = ConvertUtils.covertID(id);
        feedbackService.delete(theId);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> add(@RequestBody FeedbackRequest feedbackRequest) {
        Feedback feedback = feedbackMapper.mapFeedbackRequestToFeedback(feedbackRequest);
        Booking booking = bookingService.findById(feedbackRequest.getBookingID());
        feedback.setBooking(booking);
        feedback.setStatus(Status.Feedback.ACTIVE.name());
        feedbackService.save(feedback);
        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
}
