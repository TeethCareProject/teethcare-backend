package com.teethcare.mapper;

import com.teethcare.model.entity.Appointment;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.response.AppointmentResponse;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.PatientBookingResponse;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class,
        uses = {ServiceOfClinicMapper.class, AccountMapper.class,
                UserInforMapper.class, ClinicMapper.class, DentistMapper.class,
                VoucherMapper.class})
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
    @Mapping(source = "voucher", target = "voucher", qualifiedByName = "mapVoucherToVoucherBookingResponse")
    @Mapping(target = "feedbackResponse", ignore = true)
    @Mapping(target = "finalPrice", ignore = true)
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

    @Named(value = "mapBookingToAppointmentResponse")
    @Mapping(source = "services", target = "services",
            qualifiedByName = "mapServiceOfClinicListToServiceOfClinicResponseList")
    @Mapping(source = "patient", target = "patient", qualifiedByName = "mapPatientToPatientResponseForBooking")
    @Mapping(source = "clinic", target = "clinic", qualifiedByName = "mapClinicToClinicInfoResponse")
    @Mapping(source = "preBooking", target = "preBooking", qualifiedByName = "mapBookingToBookingResponse")
    AppointmentResponse mapAppointmentToAppointmentResponse(Appointment appointment);

    @IterableMapping(qualifiedByName = "mapBookingToAppointmentResponse")
    List<AppointmentResponse> mapAppointmentListToAppointmentResponseList(List<Appointment> bookingList);

    @Named(value = "mapAppointmentToBooking")
    @Mapping(source = "mappedPreBooking", target = "mappedPreBooking", ignore = true)
    Booking mapAppointmentToBooking(Appointment appointment);

    @AfterMapping
    default void setFinalPrice(@MappingTarget BookingResponse bookingResponse, Booking booking) {
        BigDecimal finalPrice = booking.getTotalPrice();
        if (finalPrice != null) {
            if (booking.getVoucher() != null) {
                finalPrice = booking.getTotalPrice().subtract(booking.getVoucher().getDiscountValue());
            }
            bookingResponse.setFinalPrice(finalPrice.compareTo(BigDecimal.ZERO) > 0 ? finalPrice : BigDecimal.ZERO);
        } else {
            bookingResponse.setFinalPrice(null);
        }
    }
}
