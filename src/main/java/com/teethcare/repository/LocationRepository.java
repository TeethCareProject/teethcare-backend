package com.teethcare.repository;

import com.teethcare.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findLocationByWardId(int wardId);
}