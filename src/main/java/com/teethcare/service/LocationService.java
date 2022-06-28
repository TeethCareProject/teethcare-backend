package com.teethcare.service;

import com.teethcare.model.entity.Location;

public interface LocationService extends CRUDService<Location> {
    Location getLongitudeAndLatitudeFromLocation(String address, int wardId);
}
