package com.teethcare.repository;

import com.teethcare.model.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {

    List<District> findAllByProvinceId(int id);
}