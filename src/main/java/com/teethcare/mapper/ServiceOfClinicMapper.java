package com.teethcare.mapper;

import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceOfClinicResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfClinicMapper {

    ServiceOfClinicResponse mapServiceOfClinicToServiceOfClinicResponse(ServiceOfClinic serviceOfClinic);

    List<ServiceOfClinicResponse> mapServiceOfClinicListToServiceOfClinicResponseList(List<ServiceOfClinic> serviceOfClinicList);
}
