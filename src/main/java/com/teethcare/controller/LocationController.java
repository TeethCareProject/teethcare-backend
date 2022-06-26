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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@RequestMapping(path = EndpointConstant.Province.LOCATION_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    private final ProvinceService provinceService;
    private final LocationMapper locationMapper;
    @Autowired
    private ApplicationContext context;

    @Value("${googlemap.key}")
    private String keyJson;

    @GetMapping()
    public ResponseEntity<List<ProvinceResponse>> getAll() {
        List<Province> provinces = provinceService.findAll();
        return new ResponseEntity<>(locationMapper.mapProvinceListToProvinceResponseList(provinces), HttpStatus.OK);
    }

    @GetMapping("/test")
    public LocationRequest getByAddress(@RequestParam String address) throws IOException {
        GoogleMapConfig googleMapConfig = context.getBean(GoogleMapConfig.class);
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/place/textsearch/json")
                .queryParam("query", address)
                .queryParam("key", googleMapConfig.getAPIKey2().getValue())
                .build();
        log.info(uri.toUriString());
        ResponseEntity<LocationRequest> locationRequest = new RestTemplate().getForEntity(uri.toUriString(), LocationRequest.class);
        log.info(String.valueOf(locationRequest.getBody()));
        return locationRequest.getBody();
    }
}

