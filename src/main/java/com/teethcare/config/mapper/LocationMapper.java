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
    @Mapping(source = "districts", target = "districtList")
    @InheritConfiguration(name = "mapDistrictToDistrictResponse")
    ProvinceResponse mapProvinceToProvinceResponse(Province province);
    @InheritConfiguration(name = "mapProvinceToProvinceResponse")
    List<ProvinceResponse> mapProvinceListToProvinceResponseList(List<Province> provinces);
    @Mapping(source = "wards", target = "wardList")
    @InheritConfiguration(name = "mapWardToWardResponse")
    DistrictResponse mapDistrictToDistrictResponse(District province);
    @InheritConfiguration(name = "mapDistrictToDistrictResponse")
    List<DistrictResponse> mapDistrictListToDistrictResponseList(List<District> provinces);

    WardResponse mapWardToWardResponse(Ward ward);
    @InheritConfiguration(name = "mapWardToWardResponse")
    List<WardResponse> mapWardListToWardResponseList(List<Ward> wards);
}