package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.ReportFilterRequest;
import com.teethcare.repository.ReportRepository;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final FeedbackService feedbackService;

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
    public void save(Report theEntity) {
        theEntity.setStatus(Status.Report.PENDING.name());
        reportRepository.save(theEntity);

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
    public Page<Report> findByStatus(Pageable pageable, ReportFilterRequest request) {
        List<Report> list = findAll(pageable);
        list = list.stream()
                .filter(request.requestPredicate().stream().reduce(report -> true, Predicate::and))
                .collect(Collectors.toList());
        return PaginationAndSortFactory.convertToPage(list, pageable);
    }

    @Override
    public Report evaluate(int id, String status) {
        if (!status.equalsIgnoreCase(Status.Report.REJECTED.name()) && !status.equalsIgnoreCase(Status.Report.APPROVED.name())) {
            throw new BadRequestException("Report status invalid");
        }
        Report report = findById(id);
        report.setStatus(status);
        reportRepository.save(report);
        if (status.equalsIgnoreCase(Status.Report.APPROVED.name())) {
            feedbackService.delete(report.getFeedback().getId());
        }
        return report;
    }
}
