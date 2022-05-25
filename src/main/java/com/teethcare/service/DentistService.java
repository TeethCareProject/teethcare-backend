package com.teethcare.service;

import com.teethcare.model.entity.Dentist;

import java.util.List;

public interface DentistService extends CRUDService<Dentist> {
    List<Dentist> findByClinicId(int id);
    List<Dentist> findByClinicIdAndStatus(int id, String status);
    Dentist findActiveDentist(int id);

}
