package com.teethcare.service;

import com.teethcare.model.entity.Manager;

public interface ManagerService extends CRUDService<Manager> {
    Manager getActiveManager(int id);
}
