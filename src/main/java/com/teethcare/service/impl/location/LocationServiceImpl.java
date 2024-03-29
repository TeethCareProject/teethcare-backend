package com.teethcare.service.impl.location;

import com.teethcare.model.entity.Location;
import com.teethcare.repository.LocationRepository;
import com.teethcare.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

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

    @Override
    public void update(Location theEntity) {
    }
}
