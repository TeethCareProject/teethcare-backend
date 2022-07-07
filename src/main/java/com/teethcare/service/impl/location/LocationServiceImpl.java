package com.teethcare.service.impl.location;

import com.teethcare.config.googlemap.GoogleMapConfig;
import com.teethcare.exception.InternalServerError;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.location.LocationRequest;
import com.teethcare.repository.DistrictRepository;
import com.teethcare.repository.LocationRepository;
import com.teethcare.repository.ProvinceRepository;
import com.teethcare.repository.WardRepository;
import com.teethcare.service.DistrictService;
import com.teethcare.service.LocationService;
import com.teethcare.service.ProvinceService;
import com.teethcare.service.WardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final ApplicationContext context;
    private final WardRepository wardRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;

    @Override
    public List<Location> findAll() {
        //TODO: implements later
        return null;
    }

    @Override
    public Location findById(int id) {
        //TODO: implements later
        return null;
    }

    @Override
    public void save(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void delete(int id) {
        //TODO: implements later
    }

    @Override
    public void update(Location theEntity) {
        //TODO: implements later
    }

    @Override
    public void updateLongitudeAndLatitudeByFullAddress(Location location) {
        String fullAddress = location.getFullAddress();
        GoogleMapConfig googleMapConfig = context.getBean(GoogleMapConfig.class);
        UriComponents uri = null;
        try {
            uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("maps.googleapis.com")
                    .path("/maps/api/place/textsearch/json")
                    .queryParam("query", fullAddress)
                    .queryParam("key", googleMapConfig.getAPIKey2().getValue())
                    .build();
            log.info(uri.toUriString());
            ResponseEntity<LocationRequest> locationRequest = new RestTemplate().getForEntity(uri.toUriString(), LocationRequest.class);

            log.info(String.valueOf(locationRequest.getBody()));
            double latitude = Objects.requireNonNull(locationRequest.getBody()).getResults().get(0).getGeometry().getLocation().getLatitude();
            location.setLatitude(latitude);
            double longitude = Objects.requireNonNull(locationRequest.getBody()).getResults().get(0).getGeometry().getLocation().getLongitude();
            location.setLongitude(longitude);
        } catch (IOException e) {
            throw new InternalServerError("Getting clinic's location from API fail");
        }

        save(location);
    }


//    @Override
//    public Location getLongitudeAndLatitudeFromLocation(String address, int wardId) {
//        Ward ward = wardRepository.findById(wardId);
//        District district = districtRepository.findById(ward.getId());
//        Province province = provinceRepository.findById(district.getId());
//        address = address + ward.getName() + district.getName() + province.getName();
//        return null;
//    }
}
