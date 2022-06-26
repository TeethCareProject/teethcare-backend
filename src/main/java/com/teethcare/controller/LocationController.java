package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.googlemap.GoogleMapConfig;
import com.teethcare.mapper.LocationMapper;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Province;
import com.teethcare.model.request.location.LocationRequest;
import com.teethcare.model.response.LocationResponse;
import com.teethcare.model.response.ProvinceResponse;
import com.teethcare.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = EndpointConstant.Province.PROVINCE_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    private final ProvinceService provinceService;
    private final LocationMapper locationMapper;

    @GetMapping()
    public ResponseEntity<List<ProvinceResponse>> getAll() {
        List<Province> provinces = provinceService.findAll();
        return new ResponseEntity<>(locationMapper.mapProvinceListToProvinceResponseList(provinces), HttpStatus.OK);
    }

    @GetMapping()
    public void getByAddress(@RequestParam String address) throws IOException {
        GoogleMapConfig googleMapConfig = new GoogleMapConfig();
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/place/textsearch/")
                .queryParam("key", googleMapConfig.getAPIKey())
                .queryParam("query", address)
                .build();
        log.info(uri.toUriString());
//        ResponseEntity<LocationRequest> locationRequest = new RestTemplate().getForEntity(uri.toUriString(), LocationRequest.class);
//        log.info(String.valueOf(locationRequest.getBody()));
    }
}

