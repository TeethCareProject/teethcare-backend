package com.teethcare.service;

import com.teethcare.model.entity.District;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;
import com.teethcare.repository.DistrictRepository;
import com.teethcare.repository.ProvinceRepository;
import com.teethcare.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService{
    private ProvinceRepository provinceRepository;
    private DistrictRepository districtRepository;
    private WardRepository wardRepository;

    @Autowired
    public LocationServiceImpl(ProvinceRepository provinceRepository, DistrictRepository districtRepository,
                               WardRepository wardRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.wardRepository = wardRepository;
    }


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
}
