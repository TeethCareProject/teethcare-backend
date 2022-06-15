package com.teethcare.mapper;

import com.teethcare.model.entity.District;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;
import com.teethcare.model.response.DistrictResponse;
import com.teethcare.model.response.ProvinceResponse;
import com.teethcare.model.response.WardResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
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