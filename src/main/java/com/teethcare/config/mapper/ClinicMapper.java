package com.teethcare.config.mapper;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ClinicResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    @Mapping(source = "manager.role.id", target = "manager.roleId")
    @Mapping(source = "manager.role.name", target = "manager.roleName")
    ClinicResponse mapClinicToClinicResponse(Clinic clinic);

    List<ClinicResponse> mapClinicListToClinicResponseList(List<Clinic> clinics);
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
    Clinic mapClinicRequestToClinic(ClinicRequest dto);
}
