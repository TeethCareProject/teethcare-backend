package com.teethcare.service;

import com.teethcare.config.model.entity.Manager;

public interface ManagerService extends CRUDService<Manager> {
    public Manager getActiveManager(int id);
}
