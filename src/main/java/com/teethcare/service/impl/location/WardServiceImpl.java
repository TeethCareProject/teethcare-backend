package com.teethcare.service.impl.location;


import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Ward;
import com.teethcare.repository.WardRepository;
import com.teethcare.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WardServiceImpl implements WardService {

    @Autowired
    private WardRepository wardRepository;

    @Override
    public List<Ward> findAllByDistrictIdAndDistrictProvinceId(int districtId, int provinceId) {
        return wardRepository.findAllByDistrictIdAndDistrictProvinceId(districtId, provinceId);
    }

    @Override
    public Ward findById(int id) {
        Optional<Ward> ward = wardRepository.findById(id);
        if (ward.isEmpty()) {
            throw new NotFoundException("Ward id " + id + " not found!");
        }
        return ward.get();
    }

    @Override
    public List<Ward> findAll() {
        return wardRepository.findAll();
    }
}
