package com.teethcare.service;

import com.teethcare.model.entity.District;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Province;
import com.teethcare.model.entity.Ward;

public interface LocationService extends CRUDService<Location> {
    Ward getWardById(int id);
    Province getProvinceById(int id);
    District getDistrictById(int id);

}