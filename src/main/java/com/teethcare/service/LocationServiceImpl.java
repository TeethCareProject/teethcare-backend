package com.teethcare.service;

import com.teethcare.config.model.entity.District;
import com.teethcare.config.model.entity.Location;
import com.teethcare.config.model.entity.Province;
import com.teethcare.config.model.entity.Ward;
import com.teethcare.repository.DistrictRepository;
import com.teethcare.repository.LocationRepository;
import com.teethcare.repository.ProvinceRepository;
import com.teethcare.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private LocationRepository locationRepository;



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
    public Optional<Location> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location delete(Integer id) {
        return null;
    }
}
