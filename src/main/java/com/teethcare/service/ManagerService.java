package com.teethcare.service;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ManagerResponse;

public interface ManagerService extends CRUDService<Manager> {
    Manager getActiveManager(int id);

    Clinic addNew(ManagerRegisterRequest managerRegisterRequest);
}
