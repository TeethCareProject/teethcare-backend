package com.teethcare.service;

import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.District;
import com.teethcare.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Override
    public List<District> findAllByProvinceId(int id) {
        return districtRepository.findAllByProvinceId(id);
    }

    @Override
    public District findById(int id) {
        Optional<District> district = districtRepository.findById(id);
        if (district.isEmpty()) {
            throw new IdNotFoundException();
        }
        return district.get();
    }
}

