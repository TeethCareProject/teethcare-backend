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
    @Mapping(source = "services", target = "orderDetails", qualifiedByName = "mapServicesToOrderDetail")
    @Mapping(source = "clinic.name", target = "clinicName")
    @Mapping(source = "clinic.taxCode", target = "clinicTaxCode")
    @Mapping(source = "clinic.taxCode", target = "clinicEmail")
    @Mapping(target = "clinicLocation", expression = "java(booking.getClinic().getLocation().getFullAddress())")
    Order mapBookingToOrder(Booking booking);

    @Named(value = "mapServicesToOrderDetail")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "serviceName")
    @Mapping(source = "id", target = "serviceId")
    @Mapping(source = "price", target = "servicePrice")
    OrderDetail mapServicesToOrderDetail(ServiceOfClinic serviceOfClinic);

    @InheritConfiguration(name = "mapServicesToOrderDetail")
    @Named(value = "mapServicesToOrderDetail")
    List<OrderDetail> mapServicesToOrderDetail(List<ServiceOfClinic> serviceOfClinicList);
}
