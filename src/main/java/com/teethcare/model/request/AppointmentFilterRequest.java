package com.teethcare.model.request;

import com.teethcare.model.entity.Appointment;
import com.teethcare.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class AppointmentFilterRequest {
    Integer appointmentId;
    String patientPhone;
    String patientName;
    String clinicName;

    public Predicate<Appointment> getPredicate() {
        Predicate<Appointment> predicate = appointment -> true;
        if (appointmentId != null) {
            predicate = predicate.and(appointment -> Integer.toString(appointment.getId()).
                    contains(Integer.toString(appointmentId)));
        }

        if (patientName != null) {
            predicate = predicate.and(appointment -> StringUtils.containsIgnoreCase
                    (appointment.getPatient().getFirstName() + appointment.getPatient().getLastName(), patientName));
        }

        if (patientPhone != null) {
            predicate = predicate.and(appointment -> StringUtils.containsIgnoreCase(appointment.getPatient().getPhone(), patientPhone));
        }

        if (clinicName != null) {
            predicate = predicate.and(appointment -> StringUtils.containsIgnoreCase(appointment.getClinic().getName(), clinicName));
        }

        return predicate;
    }
}
