package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackResponse;
import com.teethcare.service.BookingService;
import com.teethcare.service.FeedbackService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping(path = EndpointConstant.Feedback.FEEDBACK_ENDPOINT)
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;
    private final BookingService bookingService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountMapper accountMapper;
    private final ClinicMapper clinicMapper;

    @GetMapping("/{clinicID}")
    public ResponseEntity<Page<FeedbackResponse>> getAll(@RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                         @PathVariable("clinicID") String id,
                                                         @RequestParam(name = "ratingScore", required = false) Integer ratingScore,
                                                         @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                         @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                         @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);

        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            account = jwtTokenUtil.getAccountFromJwt(token);
        }
        int clinicID = ConvertUtils.covertID(id);
        Page<Feedback> feedbacks = feedbackService.findAllByClinicID(pageable, clinicID, account, ratingScore);
        Page<FeedbackResponse> responses = feedbacks.map(new Function<Feedback, FeedbackResponse>() {
            @Override
            public FeedbackResponse apply(Feedback feedback) {
                FeedbackResponse response = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
                response.setPatientResponse(accountMapper.mapPatientToPatientResponse(feedback.getBooking().getPatient()));
                response.setClinicInfoResponse(clinicMapper.mapClinicToClinicInfoResponse(feedback.getBooking().getClinic()));

                return response;
            }
        });
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> add(@RequestBody FeedbackRequest feedbackRequest) {
        Feedback feedback = feedbackMapper.mapFeedbackRequestToFeedback(feedbackRequest);
        Booking booking = bookingService.findById(feedbackRequest.getBookingID());
        feedback.setBooking(booking);
        feedbackService.save(feedback);
        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
        feedbackResponse.setPatientResponse(accountMapper.mapPatientToPatientResponse(booking.getPatient()));
        feedbackResponse.setClinicInfoResponse(clinicMapper.mapClinicToClinicInfoResponse(booking.getClinic()));

        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
}
