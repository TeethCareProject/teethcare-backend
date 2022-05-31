package com.teethcare.mapper;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.PatientBookingResponse;
import org.mapstruct.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper(componentModel = "spring", uses = ServiceOfClinicMapper.class)
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
    @Mapping(target = "desiredCheckingTime", ignore = true)
    @Mapping(source = "description", target = "description")
    PatientBookingResponse mapBookingToPatientBookingResponse(Booking booking);

    static long mapDateTimeToLong(Timestamp dateTime) {
        if (dateTime != null) {
            return  dateTime.getTime();
        } else {
            return 0;
        }

    }

    @Named(value = "mapBookingToBookingResponse")
    @Mapping(source = "services", target = "services",
            qualifiedByName = "mapServiceListToServiceResponseListWithoutFields")
    @Mapping(source = "dentist.id", target = "dentistId")
    @Mapping(source = "dentist.firstName", target = "dentistName")
    @Mapping(source = "customerService.id", target = "customerServiceId")
    @Mapping(source = "customerService.firstName", target = "customerServiceName")
    BookingResponse mapBookingToBookingResponse(Booking booking);

    @IterableMapping(qualifiedByName = "mapBookingToBookingResponse")
    List<BookingResponse> mapBookingListToBookingResponseList(List<Booking> bookingList);

    @Named(value = "mapBookingToBookingResponseWithoutService")
    @Mapping(source = "services", target = "services", ignore = true)
    BookingResponse mapBookingToBookingResponseWithoutService(Booking booking);

    @IterableMapping(qualifiedByName = "mapBookingToBookingResponseWithoutService")
    List<BookingResponse> mapBookingListToBookingResponseListWithoutService(List<Booking> bookingList);
}
