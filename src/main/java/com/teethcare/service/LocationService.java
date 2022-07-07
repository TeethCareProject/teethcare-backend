package com.teethcare.service;

import com.teethcare.model.dto.LocationDTO;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;

public interface LocationService extends CRUDService<Location> {
    void updateLongitudeAndLatitudeByFullAddress(Location location);
}
