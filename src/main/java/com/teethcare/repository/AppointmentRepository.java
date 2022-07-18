package com.teethcare.repository;

import com.teethcare.model.entity.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findAllByStatusInAndClinicId(List<String> status, int clinicId, Sort sort);

    List<Appointment> findAllByStatusInAndPatientId(List<String> status, int patientId, Sort sort);

    Appointment findAppointmentByStatusInAndId(List<String> status, int id);

    Appointment findAppointmentByPreBookingId(int id);
    List<Appointment> findAllByExpireAppointmentDate(Timestamp expireDate);
}
