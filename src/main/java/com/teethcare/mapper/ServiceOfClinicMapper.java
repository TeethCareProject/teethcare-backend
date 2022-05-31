package com.teethcare.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceDetailResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfClinicMapper {

    ServiceOfClinicResponse mapServiceOfClinicToServiceOfClinicResponse(ServiceOfClinic serviceOfClinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clinic", ignore = true)
    ServiceDetailResponse mapServiceOfClinicToServiceDetailResponse(ServiceOfClinic serviceOfClinic);
}
