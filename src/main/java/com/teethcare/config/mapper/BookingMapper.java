package com.teethcare.config.mapper;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.request.BookingRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking mapBookingRequestToBooking (BookingRequest bookingRequest);
}
