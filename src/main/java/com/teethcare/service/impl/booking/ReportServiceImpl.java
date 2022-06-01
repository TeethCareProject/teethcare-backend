package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.ReportFilterRequest;
import com.teethcare.repository.ReportRepository;
import com.teethcare.service.FeedbackService;
import com.teethcare.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        report.setStatus(Status.Report.APPROVED.name());
        reportRepository.save(report);
    }

    @Override
    public void update(Report theEntity) {

    }

    @Override
    public List<Report> findAll(Pageable pageable) {
        return reportRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public Page<Report> findByStatus(Pageable pageable, ReportFilterRequest request) {
        List<Report> list = findAll(pageable);
        if (request.getId() != null) {
            Predicate<Report> byID = report -> Integer.toString(report.getId()).contains(Integer.toString(request.getId()));
            list = list.stream()
                    .filter(byID)
                    .collect(Collectors.toList());
        }
        if (request.getClinicID() != null) {
            Predicate<Report> byClinicID = report -> report.getFeedback().getBooking().getClinic().getId() == request.getClinicID();
            list = list.stream()
                    .filter(byClinicID).collect(Collectors.toList());
        }
        if (request.getStatus() != null) {
            Predicate<Report> byStatus = report -> report.getStatus().equalsIgnoreCase(request.getStatus());
            list = list.stream()
                    .filter(byStatus)
                    .collect(Collectors.toList());
        }
        if (request.getClinicName() != null) {
            Predicate<Report> byClinicName = report -> report.getFeedback().getBooking().getClinic().getName().toLowerCase().contains(request.getClinicName().toLowerCase());
            list = list.stream()
                    .filter(byClinicName)
                    .collect(Collectors.toList());
        }
        return new PageImpl<>(list);
    }

    @Override
    public Report evaluate(int id, String status) {
        if (!status.equalsIgnoreCase(Status.Report.REJECTED.name()) && !status.equalsIgnoreCase(Status.Report.APPROVED.name())){
            throw new BadRequestException("Report status invalid");
        }
        Report report = findById(id);
        report.setStatus(status);
        reportRepository.save(report);
        if (status.equalsIgnoreCase(Status.Report.APPROVED.name())){
            feedbackService.delete(report.getFeedback().getId());
        }
        return report;
    }
}
