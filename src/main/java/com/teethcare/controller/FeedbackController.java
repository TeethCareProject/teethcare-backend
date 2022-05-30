package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackResponse;
import com.teethcare.service.BookingService;
import com.teethcare.service.FeedbackService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
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

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAll(@RequestParam(name = "status", required = false) String status,
                                                         @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                         @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                         @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction){
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        List<Feedback> list = new ArrayList<>();
        if (status != null){
            list = feedbackService.findByStatus(pageable, status);
        }else {
            list = feedbackService.findAll(pageable);
        }
        List<FeedbackResponse> feedbackResponses = feedbackMapper.mapFeedbackListToFeedbackResponseList(list);
        if (!list.isEmpty()) {
            return new ResponseEntity(feedbackResponses, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getById(@PathVariable("id") String id){
        int theId = ConvertUtils.covertID(id);
        Feedback feedback = feedbackService.findById(theId);
        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
        if (feedback != null){
            return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
        }else{
            throw new NotFoundException("Feedback was not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id){
        int theId = ConvertUtils.covertID(id);
        feedbackService.delete(theId);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> add(@RequestBody FeedbackRequest feedbackRequest){
        Feedback feedback = feedbackMapper.mapFeedbackRequestToFeedback(feedbackRequest);
        Booking booking = bookingService.findById(feedbackRequest.getBookingID());
        if(booking == null){
            throw new NotFoundException("Booking was not found.");
        }
        feedback.setBooking(booking);
        feedback.setStatus(Status.ACTIVE.name());
        feedbackService.save(feedback);
        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
}
