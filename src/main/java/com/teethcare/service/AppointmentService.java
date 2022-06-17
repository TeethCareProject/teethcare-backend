package com.teethcare.service;

import com.teethcare.model.entity.Appointment;
import com.teethcare.model.request.AppointmentFilterRequest;
import com.teethcare.model.request.AppointmentRequest;
import com.teethcare.model.request.AppointmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService extends CRUDService<Appointment> {
    Appointment createAppointment(String jwtToken, AppointmentRequest appointmentRequest);
    Appointment findAppointmentById(int id);
    Page<Appointment> findAllWithFilter(String jwtToken, Pageable pageable, AppointmentFilterRequest appointmentFilterRequest);
    void deleteByCSAndId(String jwtToken, int id);
    void updateByCSAndId(String jwtToken, int id, AppointmentUpdateRequest appointmentUpdateRequest);
}
