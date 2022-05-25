package com.teethcare.service;

import com.teethcare.model.entity.District;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;
import com.teethcare.repository.DistrictRepository;
import com.teethcare.repository.LocationRepository;
import com.teethcare.repository.ProvinceRepository;
import com.teethcare.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final LocationRepository locationRepository;


    @Override
    public Ward getWardById(int id) {
        return wardRepository.getWardById(id);
    }

    @Override
    public Province getProvinceById(int id) {
        return provinceRepository.getProvinceById(id);
    }

    @Override
    public District getDistrictById(int id) {
        return districtRepository.getDistrictById(id);
    }

    @Override
    public List<Location> findAll() {
        return null;
    }

    @Override
    public Location findById(int id) {
        return null;
    }

    @Override
    public void save(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void delete(int id) {
    }
}
