package com.teethcare.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ServiceRequest;
import com.teethcare.model.response.ServiceDetailResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface ServiceOfClinicMapper {

    @Named(value =  "mapServiceOfClinicToServiceOfClinicResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ServiceOfClinicResponse mapServiceOfClinicToServiceOfClinicResponse(ServiceOfClinic serviceOfClinic);
    @Named(value =  "mapServiceOfClinicListToServiceOfClinicResponseList")
    List<ServiceOfClinicResponse> mapServiceOfClinicListToServiceOfClinicResponseList(List<ServiceOfClinic> serviceOfClinics);
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    ServiceOfClinic mapServiceRequestToServiceOfClinic(ServiceRequest serviceRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateServiceOfClinicFromServiceRequest(ServiceRequest serviceRequest, @MappingTarget ServiceOfClinic service);
}
