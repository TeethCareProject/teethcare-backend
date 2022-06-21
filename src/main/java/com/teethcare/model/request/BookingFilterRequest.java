package com.teethcare.model.request;

import com.teethcare.model.entity.Booking;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class BookingFilterRequest {
    Integer bookingId;
    String patientPhone;
    String patientName;
    Integer customerServiceId;
    Integer dentistId;
    String clinicName;
    String status;
    Boolean isConfirmed;

    public Predicate<Booking> getPredicate() {
        Predicate<Booking> predicate = booking -> true;
        if (bookingId != null) {
            predicate = predicate.and(booking -> Integer.toString(booking.getId()).
                    contains(Integer.toString(getBookingId())));
        }

        if (status != null) {
            predicate = predicate.and(booking -> getStatus().equalsIgnoreCase(booking.getStatus()));
        }

        if (isConfirmed != null) {
            predicate = predicate.and(booking -> getIsConfirmed() == booking.isConfirmed());
        }

        if (patientName != null) {
            predicate = predicate.and(booking -> (booking.getPatient().getFirstName() + booking.getPatient().getLastName()).toLowerCase()
                    .contains(getPatientName().toLowerCase()));
        }

        if (patientPhone != null) {
            predicate = predicate.and(booking -> booking.getPatient().getPhone().contains(getPatientPhone()));
        }

        if (dentistId != null) {
            predicate = predicate.and(booking -> booking.getDentist() != null).
                    and(booking -> booking.getDentist().getId() == getDentistId());
        }

        if (customerServiceId != null) {
            predicate = predicate.and(booking -> booking.getCustomerService() != null).
                    and(booking -> booking.getCustomerService().getId() == getCustomerServiceId());
        }

        if (clinicName != null) {
            predicate = predicate.and(booking -> booking.getClinic().getName().toLowerCase()
                    .contains(getClinicName().toLowerCase()));
        }

        return predicate;
    }
}
