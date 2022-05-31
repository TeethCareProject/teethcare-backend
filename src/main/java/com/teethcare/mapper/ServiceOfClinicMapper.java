package com.teethcare.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfClinicMapper {

    ServiceOfClinicResponse mapServiceToServiceResponse(ServiceOfClinic serviceOfClinic);

    List<ServiceOfClinicResponse> mapServiceListToServiceResponseList(List<ServiceOfClinic> serviceOfClinicList);

    @Named("mapServiceListToServiceResponseListWithoutFields")
    @IterableMapping(qualifiedByName = "mapServiceToServiceResponseWithoutFields")
    List<ServiceOfClinicResponse> mapServiceListToServiceResponseListWithoutFields(List<ServiceOfClinic> serviceOfClinicList);

    @Named("mapServiceToServiceResponseWithoutFields")
    @Mapping(target = "money", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "clinic", ignore = true)
    ServiceOfClinicResponse mapServiceToServiceResponseWithoutFields(ServiceOfClinic serviceOfClinic);
}
