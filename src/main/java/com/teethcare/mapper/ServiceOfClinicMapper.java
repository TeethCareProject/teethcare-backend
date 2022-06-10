package com.teethcare.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceDetailResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfClinicMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ServiceOfClinicResponse mapServiceOfClinicToServiceOfClinicResponse(ServiceOfClinic serviceOfClinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "clinic", target = "clinic")
    ServiceDetailResponse mapServiceOfClinicToServiceDetailResponse(ServiceOfClinic serviceOfClinic);

    ServiceOfClinicResponse mapServiceToServiceResponse(ServiceOfClinic serviceOfClinic);

    @Named("mapServiceListToServiceResponseListWithoutFields")
    @IterableMapping(qualifiedByName = "mapServiceToServiceResponseWithoutFields")
    List<ServiceOfClinicResponse> mapServiceListToServiceResponseListWithoutFields(List<ServiceOfClinic> serviceOfClinicList);

    @Named("mapServiceToServiceResponseWithoutFields")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "duration", ignore = true)
    ServiceOfClinicResponse mapServiceToServiceResponseWithoutFields(ServiceOfClinic serviceOfClinic);
}
