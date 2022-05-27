package com.teethcare.service;

import com.teethcare.model.entity.District;

import java.util.List;

public interface DistrictService  {
    District findById(int id);
    List<District> findAllByProvinceId(int id);
}
