package com.teethcare.service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T> {
    public List<T> findAll();

    public Optional<T> findById(Integer id);

    public ResponseEntity save(T item) ;

    public ResponseEntity delete(Integer id);

}
