package com.teethcare.mapper;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    @Mapping(source = "manager.role.id", target = "manager.roleId")
    @Mapping(source = "manager.role.name", target = "manager.roleName")
    @Mapping(source = "manager.dateOfBirth", target = "manager.dateOfBirth", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "serviceOfClinic", target = "serviceOfClinicResponses")
    ClinicResponse mapClinicToClinicResponse(Clinic clinic);


    @Mapping(target = "ward", source = "ward")
    @Mapping(target = "address",
            expression = "java(location.getAddressString() + \", \" + location.getWard().getName() + \", \" " +
                    "+ location.getWard().getDistrict().getName() + \", \" " +
                    "+ location.getWard().getDistrict().getProvince().getName())")
    LocationResponse mapLocationToLocationResponse(Location location);

    List<ClinicResponse> mapClinicListToClinicResponseList(List<Clinic> clinics);

    @Mapping(source = "location", target = "location")
    ClinicInfoResponse mapClinicListToClinicInfoResponse(Clinic clinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "clinicName", target = "name")
    @Mapping(source = "clinicTaxCode", target = "taxCode")
    Clinic mapManagerRegisterRequestListToClinic(ManagerRegisterRequest managerRegisterRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "avgRatingScore", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    Clinic mapClinicRequestToClinic(ClinicRequest clinicRequest);

    ServiceOfClinicResponse mapServiceToServiceResponse(ServiceOfClinic serviceOfClinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClinicLoginResponse mapClinicToClinicLoginResponse(Clinic clinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClinicInfoResponse mapClinicToClinicInfoResponse(Clinic clinic);

    @Named(value = "mapClinicToClinicSimpleResponse")
    ClinicSimpleResponse mapClinicToClinicSimpleResponse(Clinic clinic);
}
