package com.teethcare.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface CRUDService<T> {
     List<T> findAll();

     Optional<T> findById(Integer id);

     T save(T item) ;

     T delete(Integer id);

}
