package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Feedback;
import com.teethcare.model.entity.Report;
import com.teethcare.model.request.ReportFilterRequest;
import com.teethcare.model.request.ReportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService extends CRUDService<Report> {
    List<Report> findAll(Pageable pageable);
    Page<Report> findByStatus(Pageable pageable, ReportFilterRequest request);
    Report evaluate(int id, String status);
    List<Report> findReportByFeedback(Feedback feedback);

    Report add(ReportRequest request, Account account, Feedback feedback);
}
