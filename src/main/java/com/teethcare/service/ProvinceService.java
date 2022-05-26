package com.teethcare.service;

import com.teethcare.model.entity.Province;

import java.util.List;

public interface ProvinceService  {
    Province findById(int id);
    List<Province> findAll();
}
