package com.teethcare.repository;

import com.teethcare.model.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    District getDistrictById(int id);
}