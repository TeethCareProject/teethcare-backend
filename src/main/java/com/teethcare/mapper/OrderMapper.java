package com.teethcare.mapper;

import com.teethcare.model.dto.booking.OrderDTO;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Order;
import com.teethcare.model.entity.OrderDetail;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface OrderMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "services", target = "orderDetails", qualifiedByName = "mapServicesListToOrderDetailList")
    @Mapping(source = "clinic.name", target = "clinicName")
    @Mapping(source = "clinic.taxCode", target = "clinicTaxCode")
    @Mapping(source = "clinic.email", target = "clinicEmail")
    @Mapping(source = "clinic.phone", target = "clinicPhone")
    @Mapping(source = "voucher.voucherCode", target = "voucherCode")
    @Mapping(source = "voucher.discountValue", target = "discountValue")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.gender", target = "patientGender")
    @Mapping(source = "patient.dateOfBirth", target = "patientDateOfBirth")
    @Mapping(source = "patient.phone", target = "patientPhone")
    @Mapping(source = "patient.email", target = "patientEmail")
    @Mapping(target = "patientFirstName", source = "patient.firstName")
    @Mapping(target = "patientLastName", source = "patient.lastName")
    @Mapping(target = "patientAddress", expression = "java(booking.getPatient().getLocation().getFullAddress())")
    @Mapping(source = "dentist.id", target = "dentistId")
    @Mapping(target = "dentistFirstName", source = "dentist.firstName")
    @Mapping(target = "dentistLastName", source = "dentist.lastName")
    @Mapping(source = "customerService.id", target = "customerServiceId")
    @Mapping(target = "customerServiceLastName", source = "customerService.lastName")
    @Mapping(target = "customerServiceFirstName", source = "customerService.firstName")
    @Mapping(target = "clinicLocation", expression = "java(booking.getClinic().getLocation().getFullAddress())")
    Order mapBookingToOrder(Booking booking);

    @Named(value = "mapServicesToOrderDetail")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "serviceName")
    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "price", target = "servicePrice")
    @Mapping(target = "id", ignore = true)
    OrderDetail mapServicesToOrderDetail(ServiceOfClinic serviceOfClinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "orderDetails", target = "orderDetailDTOs")
    OrderDTO mapOrderToOrderDTO(Order order);

//    @Named(value = "mapOrderDetailToOrderDetailDTO")
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    OrderDetailDTO mapOrderDetailToOrderDetailDTO(OrderDetail orderDetail);

    @Named(value = "mapServicesListToOrderDetailList")
    List<OrderDetail> mapServicesListToOrderDetailList(List<ServiceOfClinic> serviceOfClinicList);

    default OrderDetail map(ServiceOfClinic service) {
        return mapServicesToOrderDetail(service);
    }

    @Named(value = "mapOrderToBookingResponse")
    @Mapping(source = "orderDetails", target = "services",
            qualifiedByName = "mapOrderDetailListToServiceResponseListWithoutFields")
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "patient.firstName", source = "patientFirstName")
    @Mapping(target = "patient.lastName", source = "patientLastName")
    @Mapping(target = "patient.dateOfBirth", source = "patientDateOfBirth")
    @Mapping(target = "patient.phone", source = "patientPhone")
    @Mapping(target = "patient.gender", source = "patientGender")
    @Mapping(target = "patient.email", source = "patientEmail")
    @Mapping(target = "dentist.id", source = "dentistId")
    @Mapping(target = "dentist.firstName", source = "dentistFirstName")
    @Mapping(target = "dentist.lastName", source = "dentistLastName")
    @Mapping(target = "customerService.id", source = "customerServiceId")
    @Mapping(target = "customerService.firstName", source = "customerServiceFirstName")
    @Mapping(target = "customerService.lastName", source = "customerServiceLastName")
    @Mapping(source = "clinicName", target = "clinic.name")
    @Mapping(target = "clinic.id", ignore = true)
    @Mapping(target = "clinic.startTimeShift1", ignore = true)
    @Mapping(target = "clinic.endTimeShift1", ignore = true)
    @Mapping(target = "clinic.startTimeShift2", ignore = true)
    @Mapping(target = "clinic.endTimeShift2", ignore = true)
    @Mapping(source = "discountValue", target = "voucher.discountValue")
    @Mapping(source = "voucherCode", target = "voucher.voucherCode")
    @Mapping(target = "voucher.createdTime", ignore = true)
    @Mapping(target = "voucher.expiredTime", ignore = true)
    @Mapping(target = "voucher.quantity", ignore = true)
    @Mapping(target = "feedbackResponse", ignore = true)
    @Mapping(target = "finalPrice", ignore = true)
    BookingResponse mapOrderToBookingResponse(Order order);

    @Named("mapOrderDetailListToServiceResponseListWithoutFields")
    @IterableMapping(qualifiedByName = "mapOrderDetailToServiceResponseWithoutFields")
    List<ServiceOfClinicResponse> mapOrderDetailListToServiceResponseListWithoutFields(List<OrderDetail> orderDetails);

    @Named("mapOrderDetailToServiceResponseWithoutFields")
    @Mapping(target = "id", source = "serviceId")
    @Mapping(target = "name", source = "serviceName")
    @Mapping(target = "price", source = "servicePrice")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "duration", ignore = true)
    ServiceOfClinicResponse mapOrderDetailToServiceResponseWithoutFields(OrderDetail orderDetail);
}
