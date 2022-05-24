package com.teethcare.repository;

import com.teethcare.config.model.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    Province getProvinceById(int id);
}