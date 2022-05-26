package com.teethcare.service;

import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Ward;
import com.teethcare.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Override
    public List<Ward> findAllByDistrictIdAndDistrictProvinceId(int districtId, int provinceId) {
        return wardRepository.findAllByDistrictIdAndDistrictProvinceId(districtId, provinceId);
    }

    @Override
    public Ward findById(int id) {
        Optional<Ward> ward = wardRepository.findById(id);
        if (ward.isEmpty()) {
            throw new IdNotFoundException();
        }
        return ward.get();
    }
}
