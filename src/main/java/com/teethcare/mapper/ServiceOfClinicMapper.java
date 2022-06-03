package com.teethcare.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceDetailResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServiceOfClinicMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ServiceOfClinicResponse mapServiceOfClinicToServiceOfClinicResponse(ServiceOfClinic serviceOfClinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "clinic", target = "clinic")
    ServiceDetailResponse mapServiceOfClinicToServiceDetailResponse(ServiceOfClinic serviceOfClinic);
}
