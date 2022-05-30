package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Report;
import com.teethcare.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final ReportRepository reportRepository;

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
        reportRepository.save(theEntity);

    }

    @Override
    public void delete(int theId) {
        Report report = findById(theId);
        report.setStatus(Status.INACTIVE.name());
        reportRepository.save(report);
    }

    @Override
    public List<Report> findAll(Pageable pageable) {
        return reportRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public List<Report> findByStatus(Pageable pageable, String status) {
        return reportRepository.findAllByStatus(pageable, status);
    }
}
