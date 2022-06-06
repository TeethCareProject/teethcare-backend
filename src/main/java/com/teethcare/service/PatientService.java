package com.teethcare.service;

import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.PatientRegisterRequest;

public interface PatientService extends CRUDService<Patient> {
    Patient addNew(PatientRegisterRequest dentistRegisterRequest);
}
