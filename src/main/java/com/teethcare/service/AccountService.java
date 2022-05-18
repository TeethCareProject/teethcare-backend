package com.teethcare.service;

public interface AccountService<T> extends CRUDService<T>{
    T findById(String theId);

    void deleteById(String theId);
}
