package com.teethcare.repository;

import com.teethcare.model.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    Ward getWardById(int id);

    List<Ward> findAllByDistrictIdAndDistrictProvinceId(int districtId, int provinceId);

}