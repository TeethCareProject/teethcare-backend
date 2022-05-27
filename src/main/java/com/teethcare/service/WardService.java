package com.teethcare.service;


import com.teethcare.model.entity.Ward;

import java.util.List;

public interface WardService {
    Ward findById(int id);

    List<Ward> findAll();
    List<Ward> findAllByDistrictIdAndDistrictProvinceId(int districtId, int provinceId);
}
