package com.teethcare.service.impl.location;

import com.teethcare.model.entity.District;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;
import com.teethcare.repository.DistrictRepository;
import com.teethcare.repository.LocationRepository;
import com.teethcare.repository.ProvinceRepository;
import com.teethcare.repository.WardRepository;
import com.teethcare.service.DistrictService;
import com.teethcare.service.LocationService;
import com.teethcare.service.ProvinceService;
import com.teethcare.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
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
    public Location getLongitudeAndLatitudeFromLocation(String address, int wardId) {
        Ward ward = wardRepository.findById(wardId);
        District district = districtRepository.findById(ward.getId());
        Province province = provinceRepository.findById(district.getId());
        address = address + ward.getName() + district.getName() + province.getName();
        return null;
    }
}
