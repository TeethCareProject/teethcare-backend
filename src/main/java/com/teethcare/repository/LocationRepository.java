package com.teethcare.repository;

import com.teethcare.config.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}