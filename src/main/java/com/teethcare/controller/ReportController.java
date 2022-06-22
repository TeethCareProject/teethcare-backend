package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.EvaluateRequest;
import com.teethcare.model.request.ReportFilterRequest;
import com.teethcare.model.request.ReportRequest;
import com.teethcare.model.response.ReportResponse;
import com.teethcare.service.AccountService;
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

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Report.REPORT_ENDPOINT)
public class ReportController {
    private final ReportService reportService;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackService feedbackService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<Page<ReportResponse>> getAll(ReportFilterRequest request,
                                                       @RequestHeader(value = AUTHORIZATION) String token,
                                                       @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                       @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                       @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Account account = accountService.getAccountByUsername(username);

        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<Report> list = reportService.findByStatus(pageable, request , account);
        Page<ReportResponse> responses = list.map(feedbackMapper::mapReportToReportResponse);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")

    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<ReportResponse> getById(@PathVariable("id") int id,
                                                  @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Account account = accountService.getAccountByUsername(username);

        Report report = reportService.findById(id, account);

        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping

    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<ReportResponse> add(@RequestHeader(value = AUTHORIZATION) String token,
                                              @Valid @RequestBody ReportRequest reportRequest) {
        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }
        Feedback feedback = feedbackService.findById(reportRequest.getFeedbackId());
        Report report = reportService.add(reportRequest, account, feedback);

        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<ReportResponse> update(@RequestBody EvaluateRequest request,
                                                 @PathVariable("id") int id) {
        Report report = reportService.evaluate(id, request.getStatus());
        if (request.getStatus().equalsIgnoreCase(Status.Report.APPROVED.name())) {
            feedbackService.delete(report.getFeedback().getId());
        }
        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
