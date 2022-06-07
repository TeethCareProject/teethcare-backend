package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.ReportRequest;
import com.teethcare.model.response.ReportResponse;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Report.REPORT_ENDPOINT)
public class ReportController {
    private final ReportService reportService;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackService feedbackService;
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<ReportResponse>> getAll(@RequestParam(name = "status", required = false) String status,
                                                       @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                       @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                       @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction){
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        List<Report> list = new ArrayList<>();
        if (status != null){
            list = reportService.findByStatus(pageable, status);
        }else{
            list = reportService.findAll(pageable);
        }
        List<ReportResponse> reportResponseList = new ArrayList<>();
        for (Report report: list){
            ReportResponse response = feedbackMapper.mapReportToReportResponse(report);
            response.setFeedbackResponse(feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback()));
            reportResponseList.add(response);
        }
        return new ResponseEntity<>(reportResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<ReportResponse> getById(@PathVariable("id") String id){
        int theId = ConvertUtils.covertID(id);
        Report report = reportService.findById(theId);
        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);
        response.setFeedbackResponse(feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id){
        int theId = ConvertUtils.covertID(id);
        reportService.delete(theId);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority((T(com.teethcare.common.Role).MANAGER), T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity<ReportResponse> add(@RequestBody ReportRequest reportRequest){
        Report report = feedbackMapper.mapReportRequestToReport(reportRequest);
        report.setFeedback(feedbackService.findById(reportRequest.getFeedbackID()));
        report.setStatus(Status.Report.PENDING.name());
        reportService.save(report);
        ReportResponse response = feedbackMapper.mapReportToReportResponse(report);
        response.setFeedbackResponse(feedbackMapper.mapFeedbackToFeedbackResponse(report.getFeedback()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
