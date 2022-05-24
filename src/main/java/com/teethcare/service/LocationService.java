package com.teethcare.service;

import com.teethcare.config.model.entity.District;
import com.teethcare.config.model.entity.Location;
import com.teethcare.config.model.entity.Province;
import com.teethcare.config.model.entity.Ward;

public interface LocationService extends CRUDService<Location> {
    Ward getWardById(int id);
    Province getProvinceById(int id);
    District getDistrictById(int id);

}
