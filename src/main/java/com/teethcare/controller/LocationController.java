package com.teethcare.controller;

import com.teethcare.config.mapper.LocationMapper;
import com.teethcare.model.entity.Province;
import com.teethcare.model.response.ProvinceResponse;
import com.teethcare.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping(path = EndpointConstant.Province.PROVINCE_ENDPOINT)
public class LocationController {
    private final ProvinceService provinceService;
    private final LocationMapper locationMapper;

    @GetMapping(path = "/api/provinces")
    public ResponseEntity<List<ProvinceResponse>> getAll() {
        List<Province> provinces = provinceService.findAll();
        return new ResponseEntity<>(locationMapper.mapProvinceListToProvinceResponseList(provinces), HttpStatus.OK);
    }
}

