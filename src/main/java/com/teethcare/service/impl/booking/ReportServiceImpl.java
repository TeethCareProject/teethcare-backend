package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.ForbiddenException;
import com.teethcare.mapper.FeedbackMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.ReportFilterRequest;
import com.teethcare.model.request.ReportRequest;
import com.teethcare.repository.ReportRepository;
import com.teethcare.service.ReportService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public Report findById(int id) {
        Report report = reportRepository.getById(id);
        return report;
    }

    @Override
    public void save(Report report) {
        Feedback feedback = report.getFeedback();
        List<Report> reports = findReportByFeedback(feedback);
        if (reports.isEmpty() || reports.get(0).getStatus().equals(Status.Report.REJECTED.name())) {
            report.setStatus(Status.Report.PENDING.name());
            report.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            reportRepository.save(report);
        } else if(!reports.isEmpty() && reports.get(0).getStatus().equals(Status.Report.APPROVED.name())) {
            throw new BadRequestException("The report for this feedback has been approved. You cannot continue to submit reports.");
        } else if (!reports.isEmpty() && reports.get(0).getStatus().equals(Status.Report.PENDING.name())) {
            throw new BadRequestException("The report for this feedback is pending. You cannot submit reports now.");
        }
    }

    @Override
    public void delete(int theId) {
        Report report = findById(theId);
        report.setStatus(Status.Report.REJECTED.name());
        reportRepository.save(report);
    }

    @Override
    public void update(Report theEntity) {

    }

    @Override
    public List<Report> findAll(Pageable pageable) {
        return reportRepository.findAll();
    }

    @Override
    public Page<Report> findByStatus(Pageable pageable, ReportFilterRequest request, Account account) {
        List<Report> list = findAll(pageable);
        list = list.stream()
                .filter(request.requestPredicate().stream().reduce(report -> true, Predicate::and))
                .collect(Collectors.toList());
        if (account.getRole().getName().equals(Role.CUSTOMER_SERVICE.name())){
            CustomerService customerService = (CustomerService) account;
            list = list.stream()
                    .filter(report -> report.getFeedback().getBooking().getClinic() == customerService.getClinic())
                    .collect(Collectors.toList());
        }
        return PaginationAndSortFactory.convertToPage(list, pageable);
    }

    @Override
    public Report evaluate(int id, String status) {
        if (!status.equalsIgnoreCase(Status.Report.REJECTED.name()) && !status.equalsIgnoreCase(Status.Report.APPROVED.name())){
            throw new BadRequestException("Report status invalid");
        }
        Report report = findById(id);
        report.setStatus(status);
        reportRepository.save(report);
        return report;
    }

    @Override
    public List<Report> findReportByFeedback(Feedback feedback) {
        List<Report> reports = reportRepository.findReportByFeedback(feedback);
        Collections.sort(reports);
        return reports;
    }

    @Override
    public Report add(ReportRequest request, Account account, Feedback feedback) {
        CustomerService customerService = (CustomerService) account;
        Clinic clinic = feedback.getBooking().getClinic();
        if(customerService.getClinic() != clinic){
            throw new ForbiddenException("Cannot send the report for this feedback.");
        }
        Report report = feedbackMapper.mapReportRequestToReport(request);
        report.setFeedback(feedback);
        save(report);
        return report;
    }
}
