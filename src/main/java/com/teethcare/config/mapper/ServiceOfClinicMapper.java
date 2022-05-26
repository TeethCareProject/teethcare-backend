package com.teethcare.config.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfClinicMapper {

    ServiceOfClinicResponse mapServiceToServiceResponse(ServiceOfClinic serviceOfClinic);

    List<ServiceOfClinicResponse> mapServiceListToServiceResponseList(List<ServiceOfClinic> serviceOfClinicList);
}