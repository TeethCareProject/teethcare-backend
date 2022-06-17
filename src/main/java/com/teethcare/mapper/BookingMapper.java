package com.teethcare.mapper;

import com.teethcare.model.dto.BookingConfirmationDTO;
import com.teethcare.model.entity.Appointment;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.AppointmentResponse;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.PatientBookingResponse;
import org.mapstruct.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class,
        uses = {ServiceOfClinicMapper.class, AccountMapper.class,
        UserInforMapper.class, ClinicMapper.class, DentistMapper.class})
public interface BookingMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "desiredCheckingTime", ignore = true)
    Booking mapBookingRequestToBooking(BookingRequest bookingRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "bookingID")
    @Mapping(source = "patient.firstName", target = "firstName")
    @Mapping(source = "patient.lastName", target = "lastName")
    @Mapping(source = "patient.phone", target = "phoneNumber")
    @Mapping(source = "patient.gender", target = "gender")
    @Mapping(source = "patient.email", target = "email")
    @Mapping(target = "desiredCheckingTime", ignore = true)
    @Mapping(source = "description", target = "description")
    @Mapping(source = "clinic", target = "clinic")
    PatientBookingResponse mapBookingToPatientBookingResponse(Booking booking);

    @Named(value = "mapBookingToBookingResponse")
    @Mapping(source = "services", target = "services",
            qualifiedByName = "mapServiceListToServiceResponseListWithoutFields")
    @Mapping(source = "dentist", target = "dentist", qualifiedByName = "mapDentistToUserInforResponse")
    @Mapping(source = "customerService", target = "customerService", qualifiedByName = "mapAccountToUserInforResponse")
    @Mapping(source = "patient", target = "patient", qualifiedByName = "mapPatientToPatientResponseForBooking")
    @Mapping(source = "clinic", target = "clinic", qualifiedByName = "mapClinicToClinicSimpleResponse")
    BookingResponse mapBookingToBookingResponse(Booking booking);

    @IterableMapping(qualifiedByName = "mapBookingToBookingResponse")
    List<BookingResponse> mapBookingListToBookingResponseList(List<Booking> bookingList);

    @Named(value = "mapBookingToBookingResponseWithoutService")
    @Mapping(source = "services", target = "services", ignore = true)
    @Mapping(source = "dentist", target = "dentist", qualifiedByName = "mapDentistToUserInforResponse")
    @Mapping(source = "customerService", target = "customerService", qualifiedByName = "mapAccountToUserInforResponse")
    BookingResponse mapBookingToBookingResponseWithoutService(Booking booking);

    @IterableMapping(qualifiedByName = "mapBookingToBookingResponseWithoutService")
    List<BookingResponse> mapBookingListToBookingResponseListWithoutService(List<Booking> bookingList);

//    BookingConfirmationDTO mapBookingToBookingConfirmationDTO(Booking booking);

    @Named(value = "mapBookingToAppointmentResponse")
    @Mapping(source = "services", target = "services",
            qualifiedByName = "mapServiceListToServiceResponseListWithoutFields")
    @Mapping(source = "patient", target = "patient", qualifiedByName = "mapPatientToPatientResponseForBooking")
    @Mapping(source = "clinic", target = "clinic", qualifiedByName = "mapClinicToClinicSimpleResponse")
    @Mapping(source = "preBooking", target = "preBooking", qualifiedByName = "mapBookingToBookingResponse")
    AppointmentResponse mapAppointmentToAppointmentResponse(Appointment appointment);

    @IterableMapping(qualifiedByName = "mapBookingToAppointmentResponse")
    List<AppointmentResponse> mapAppointmentListToAppointmentResponseList(List<Appointment> bookingList);
}
