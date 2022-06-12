package com.teethcare.repository;

import com.teethcare.model.entity.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByStatusIsNotNullAndClinicIdAndExaminationTimeIsNull(int clinicId, Sort sort);
    List<Appointment> findAllByStatusIsNotNullAndPatientIdAndExaminationTimeIsNull(int patientId, Sort sort);
}
