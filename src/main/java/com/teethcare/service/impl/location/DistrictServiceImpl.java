package com.teethcare.service.impl.location;

import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.District;
import com.teethcare.repository.DistrictRepository;
import com.teethcare.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Override
    public District findById(int id) {
        Optional<District> district = districtRepository.findById(id);
        if (district.isEmpty()) {
            throw new NotFoundException("District id " + id + " not found!");
        }
        return district.get();
    }
}

