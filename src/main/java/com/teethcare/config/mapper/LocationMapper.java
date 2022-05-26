package com.teethcare.config.mapper;

import com.teethcare.model.entity.*;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.*;
import org.mapstruct.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    ProvinceResponse mapProvinceToProvinceResponse(Province province);
    @InheritConfiguration(name = "mapProvinceToProvinceResponse")
    List<ProvinceResponse> mapProvinceListToProvinceResponseList(List<Province> provinces);

    DistrictResponse mapDistrictToDistrictResponse(District province);
    @InheritConfiguration(name = "mapProvinceToProvinceResponse")
    List<DistrictResponse> mapDistrictListToDistrictResponseList(List<District> provinces);

    WardResponse mapWardToWardResponse(Ward ward);
    @InheritConfiguration(name = "mapProvinceToProvinceResponse")
    List<WardResponse> mapWardListToWardResponseList(List<Ward> wards);
}