package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.mapper.LocationMapper;
import com.teethcare.model.entity.District;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;
import com.teethcare.model.response.DistrictResponse;
import com.teethcare.model.response.ProvinceResponse;
import com.teethcare.model.response.WardResponse;
import com.teethcare.service.DistrictService;
import com.teethcare.service.ProvinceService;
import com.teethcare.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
//@RequestMapping(path = EndpointConstant.Province.PROVINCE_ENDPOINT)
public class LocationController {
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;
    private final LocationMapper locationMapper;

    @GetMapping(path = "/api/locationmetadata")
    public ResponseEntity<List<ProvinceResponse>> getAllProvinces() {
        List<Province> provinces = provinceService.findAll();
        return new ResponseEntity<>(locationMapper.mapProvinceListToProvinceResponseList(provinces), HttpStatus.OK);
    }

//
//    @GetMapping(path = "/{provinceId}/districts")
//    public ResponseEntity<List<DistrictResponse>> getAllDistrictsByProvinceId(@PathVariable("provinceId") int id) {
//        List<District> districts = districtService.findAllByProvinceId(id);
//        return new ResponseEntity<>(locationMapper.mapDistrictListToDistrictResponseList(districts), HttpStatus.OK);
//    }
//
//    @GetMapping(path = "/{provinceId}/districts/{districtId}/wards")
//    public ResponseEntity<List<WardResponse>> getAllWardsByDistrictId(@PathVariable("provinceId") int provinceId,
//                                                                      @PathVariable("districtId") int districtId) {
//        List<Ward> wards = wardService.findAllByDistrictIdAndDistrictProvinceId(districtId, provinceId);
//        return new ResponseEntity<>(locationMapper.mapWardListToWardResponseList(wards), HttpStatus.OK);
//    }
//
//    @GetMapping(path = "/locations")
//    public ResponseEntity<List<Province>> getAllPro() {
//        return new ResponseEntity<>(provinceService.findAll(), HttpStatus.OK);
//    }
}

