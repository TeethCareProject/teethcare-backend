package com.teethcare.service;

import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistServiceImpl implements DentistService {
    private DentistRepository dentistRepository;

    @Autowired
    public DentistServiceImpl(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @Override
    public Dentist findById(int theId) {
        Optional<Dentist> result = dentistRepository.findById(theId);

        Dentist theDentist = null;

        if (result.isPresent()) {
            theDentist = result.get();
        }
        else {
            throw new NotFoundException();
        }

        return theDentist;
    }

    @Override
    public List<Dentist> findAll() {
        return dentistRepository.findAll();
    }

    @Override
    public void save(Dentist theDentist) {
        dentistRepository.save(theDentist);
    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public List<Dentist> findByClinicId(int theId) {
        List<Dentist> dentistList = dentistRepository.findByClinicId(theId);

        if (dentistList == null || dentistList.size() == 0) {
            throw new NotFoundException();
        }

        return dentistList;
    }

    @Override
    public List<Dentist> findByClinicIdAndStatus(int theId, String status) {
        List<Dentist> dentistList = dentistRepository.findByClinicIdAndStatus(theId, status);

//        if (dentistList == null || dentistList.size() == 0) {
//            throw new NotFoundException();
//        }

        return dentistList;
    }
}
