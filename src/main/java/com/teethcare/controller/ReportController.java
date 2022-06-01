package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.EvaluateRequest;
import com.teethcare.model.request.ReportFilterRequest;
import com.teethcare.model.request.ReportRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.FeedbackResponse;
import com.teethcare.model.response.ReportResponse;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Report.REPORT_ENDPOINT)
public class ReportController {
    private final ReportService reportService;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackService feedbackService;
    private final ClinicMapper clinicMapper;
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<Page<ReportResponse>> getAll(ReportFilterRequest request,
                                                       @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                       @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                       @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction){
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        Page<Report> list = reportService.findByStatus(pageable, request);
        Page<ReportResponse> responses = list.map(new Function<Report, ReportResponse>() {
            @Override
            public ReportResponse apply(Report report) {
                ReportResponse response = feedbackMapper.mapReportToReportResponse(report);

                FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback());
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicToClinicInfoResponse(report.getFeedback().getBooking().getClinic());
                feedbackResponse.setClinicInfoResponse(clinicInfoResponse);

                response.setFeedbackResponse(feedbackResponse);
                return response;
            }
        });
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<ReportResponse> getById(@PathVariable("id") String id){
        int theId = ConvertUtils.covertID(id);
        Report report = reportService.findById(theId);
        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);

        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback());
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicToClinicInfoResponse(report.getFeedback().getBooking().getClinic());
        feedbackResponse.setClinicInfoResponse(clinicInfoResponse);

        response.setFeedbackResponse(feedbackResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority((T(com.teethcare.common.Role).MANAGER), T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<ReportResponse> add(@RequestBody ReportRequest reportRequest){
        Report report = feedbackMapper.mapReportRequestToReport(reportRequest);
        report.setFeedback(feedbackService.findById(reportRequest.getFeedbackID()));
        reportService.save(report);

        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);

        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback());
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicToClinicInfoResponse(report.getFeedback().getBooking().getClinic());
        feedbackResponse.setClinicInfoResponse(clinicInfoResponse);

        response.setFeedbackResponse(feedbackResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<ReportResponse> update(@RequestBody EvaluateRequest request,
                                                 @PathVariable("id") String id){
        int theID = ConvertUtils.covertID(id);
        Report report = reportService.evaluate(theID, request.getStatus());

        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);

        FeedbackResponse feedbackResponse = feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback());
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicToClinicInfoResponse(report.getFeedback().getBooking().getClinic());
        feedbackResponse.setClinicInfoResponse(clinicInfoResponse);

        response.setFeedbackResponse(feedbackResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
