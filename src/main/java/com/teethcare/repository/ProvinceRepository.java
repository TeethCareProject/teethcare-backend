package com.teethcare.repository;

import com.teethcare.model.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    Province getProvinceById(int id);
    List<Province> getAllByIdGreaterThan(int min);
}