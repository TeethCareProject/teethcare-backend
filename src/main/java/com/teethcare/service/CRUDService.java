package com.teethcare.service;

import java.util.List;

public interface CRUDService<T> {
    List<T> findAll();

    T findById(int theId);

    void save(T theEntity);

    void deleteById(int theId);
}
