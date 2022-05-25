package com.teethcare.service;

import java.util.List;

public interface CRUDService<T> {
    List<T> findAll();

    T findById(int id);

    void save(T theEntity);

    void delete(int theId);
}
