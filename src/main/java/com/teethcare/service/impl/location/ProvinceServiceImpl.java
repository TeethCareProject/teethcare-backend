package com.teethcare.service.impl.location;

import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Province;
import com.teethcare.repository.ProvinceRepository;
import com.teethcare.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Province findById(int id) {
        Optional<Province> province = provinceRepository.findById(id);
        if (province.isEmpty()) {
            throw new NotFoundException("Province id " + id + " not found!");
        }
        return province.get();
    }
}
