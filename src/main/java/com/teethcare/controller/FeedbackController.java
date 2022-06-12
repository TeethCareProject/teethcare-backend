package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Role;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.FeedbackRequest;
import com.teethcare.model.response.FeedbackByClinicResponse;
import com.teethcare.model.response.FeedbackResponse;
import com.teethcare.model.response.ReportResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.BookingService;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping(path = EndpointConstant.Feedback.FEEDBACK_ENDPOINT)
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

    @GetMapping("/{clinicID}")
    public ResponseEntity<Page<FeedbackByClinicResponse>> getAll(@RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                                 @PathVariable("clinicID") int clinicId,
                                                                 @RequestParam(name = "ratingScore", required = false) Integer ratingScore,
                                                                 @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                 @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                 @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);

        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }
        Page<Feedback> feedbacks = feedbackService.findAllByClinicID(pageable, clinicId, account, ratingScore);
        Page<FeedbackByClinicResponse> responses = feedbacks.map(feedbackMapper::mapFeedbackToFeedbackByClinicResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<FeedbackResponse> add(@RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                @RequestBody FeedbackRequest feedbackRequest) {
        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }
        Feedback feedback = feedbackService.addFeedback(feedbackRequest, account);
        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(feedback);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<FeedbackByClinicResponse> getById(@RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                    @RequestParam(name = "id") int id) {
        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }
        Feedback feedback = feedbackService.findById(id, account);
        FeedbackByClinicResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackByClinicResponse(feedback);
        return new ResponseEntity<>(feedbackResponse, HttpStatus.OK);
    }
}
