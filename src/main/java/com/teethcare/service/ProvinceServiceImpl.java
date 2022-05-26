package com.teethcare.service;

import com.teethcare.config.mapper.LocationMapper;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;
import com.teethcare.model.response.ProvinceResponse;
import com.teethcare.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Province findById(int id) {
        Optional<Province> province = provinceRepository.findById(id);
        if (province.isEmpty()) {
            throw new IdNotFoundException();
        }
        return province.get();
    }
}
