package com.teethcare.mapper;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.PatientBookingResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "desiredCheckingTime", ignore = true)
    Booking mapBookingRequestToBooking (BookingRequest bookingRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "patient.firstName", target = "firstName")
    @Mapping(source = "patient.lastName", target = "lastName")
    @Mapping(source = "patient.phone", target = "phoneNumber")
    @Mapping(source = "patient.gender", target = "gender")
    @Mapping(source = "patient.email", target = "email")
    @Mapping(source = "desiredCheckingTime", target = "desiredCheckingTime")
    @Mapping(source = "description", target = "description")
    PatientBookingResponse mapBookingToPatientBookingResponse(Booking booking);
}
