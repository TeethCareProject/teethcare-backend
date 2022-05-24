package com.teethcare.service;

import com.teethcare.model.entity.Manager;

public interface ManagerService extends CRUDService<Manager> {
    public Manager getActiveManager(int id);
}
