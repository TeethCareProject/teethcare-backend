package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T> {
    public List<T> findAll();

    public Optional<T> findById(Integer id);

    public T save(T item) ;

    public ResponseEntity delete(Integer id);

}
