package com.teethcare.model.request;

import com.teethcare.model.entity.Booking;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.function.Predicate;
import com.teethcare.common.Status;

@Getter
@Setter
public class ClinicStatisticRequest {
    private long startDate;
    private long endDate;

    public Predicate<Booking> rangeTimePredicate(){
        Timestamp startDateTmp = new Timestamp(startDate);
        Timestamp endDateTmp = new Timestamp(endDate);
        Predicate<Booking> predicates = booking -> { return booking.getCreateBookingTime().compareTo(startDateTmp) >= 0 && booking.getCreateBookingTime().compareTo(endDateTmp) <= 0;};
        return predicates;
    }

    public Predicate<Booking> pendingStatusPredicate(){
        Predicate<Booking> predicates = booking -> { return booking.getStatus().equals(Status.Booking.PENDING.name());};
        return predicates;
    }

    public Predicate<Booking> processingStatusPredicate(){
        Predicate<Booking> predicates = booking -> { return (booking.getStatus().equals(Status.Booking.TREATMENT.name()) || booking.getStatus().equals(Status.Booking.REQUEST.name()));};
        return predicates;
    }

    public Predicate<Booking> doneStatusPredicate(){
        Predicate<Booking> predicates = booking -> { return booking.getStatus().equals(Status.Booking.DONE.name());};
        return predicates;
    }
    public Predicate<Booking> failedStatusPredicate(){
        Predicate<Booking> predicates = booking -> { return booking.getStatus().equals(Status.Booking.REJECTED.name()) || booking.getStatus().equals(Status.Booking.EXPIRED.name())
                || booking.getStatus().equals(Status.Booking.UNAVAILABLE.name());};
        return predicates;
    }
}
