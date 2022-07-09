package com.teethcare.mapper;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Order;
import com.teethcare.model.entity.OrderDetail;
import com.teethcare.model.entity.ServiceOfClinic;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface OrderMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "services", target = "orderDetails", qualifiedByName = "mapServicesListToOrderDetailList")
    @Mapping(source = "clinic.name", target = "clinicName")
    @Mapping(source = "clinic.taxCode", target = "clinicTaxCode")
    @Mapping(source = "clinic.email", target = "clinicEmail")
    @Mapping(source = "voucher.voucherCode", target = "voucherCode")
    @Mapping(source = "voucher.discountValue", target = "discountValue")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.gender", target = "patientGender")
    @Mapping(source = "patient.dateOfBirth", target = "patientDateOfBirth")
    @Mapping(source = "patient.phone", target = "patientPhone")
    @Mapping(source = "patient.email", target = "patientEmail")
    @Mapping(target = "patientName", expression = "java(booking.getPatient().getFullName())")
    @Mapping(target = "patientAddress", expression = "java(booking.getPatient().getLocation().getFullAddress())")
    @Mapping(source = "dentist.id", target = "dentistId")
    @Mapping(target = "dentistName", expression = "java(booking.getDentist().getFullName())")
    @Mapping(source = "customerService.id", target = "customerServiceId")
    @Mapping(target = "customerServiceName", expression = "java(booking.getCustomerService().getFullName())")
    @Mapping(target = "clinicLocation", expression = "java(booking.getClinic().getLocation().getFullAddress())")
    Order mapBookingToOrder(Booking booking);

    @Named(value = "mapServicesToOrderDetail")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "serviceName")
    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "price", target = "servicePrice")
    @Mapping(target = "id", ignore = true)
    OrderDetail mapServicesToOrderDetail(ServiceOfClinic serviceOfClinic);

    @Named(value = "mapServicesListToOrderDetailList")
    List<OrderDetail> mapServicesListToOrderDetailList(List<ServiceOfClinic> serviceOfClinicList);

    default OrderDetail map (ServiceOfClinic service) {
        return mapServicesToOrderDetail(service);
    }
}
