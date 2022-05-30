package com.teethcare.service;

import com.teethcare.model.entity.Report;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService extends CRUDService<Report> {
    List<Report> findAll(Pageable pageable);
    List<Report> findByStatus(Pageable pageable, String status);
}
