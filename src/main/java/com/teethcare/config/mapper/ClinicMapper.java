package com.teethcare.config.mapper;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Role;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ClinicResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    @Mapping(source = "manager.role", target = "manager.role", qualifiedByName = "mapRoleToString")
    ClinicResponse mapClinicToClinicResponse(Clinic clinic);

    List<ClinicResponse> mapClinicListToClinicResponseList(List<Clinic> clinics);
    ClinicInfoResponse mapClinicListToClinicInfoResponse(Clinic clinic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "clinicName", target = "name")
    @Mapping(source = "clinicTaxCode", target = "taxCode")
    Clinic mapManagerRegisterRequestListToClinic(ManagerRegisterRequest managerRegisterRequest);

    @Named("mapRoleToString")
    static String mapRoleToString(Role role) {
        return role.getName();
    }
}