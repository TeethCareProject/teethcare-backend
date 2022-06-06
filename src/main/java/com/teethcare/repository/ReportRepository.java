package com.teethcare.repository;

import com.teethcare.model.entity.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByStatusIsNotNull(Pageable pageable);
    List<Report> findAllByStatus(Pageable pageable, String status);

    List<Report> findAll();
}
