package com.teethcare.service;

import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.repository.DentistRepository;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistServiceImpl implements AccountService<Dentist> {
    private DentistRepository dentistRepository;

    @Autowired
    public DentistServiceImpl(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @Override
    public Dentist findById(String theId) {
        Optional<Dentist> result = dentistRepository.findById(theId);

        Dentist theDentist = null;

        if (result.isPresent()) {
            theDentist = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theDentist;
    }

    @Override
    public void deleteById(String theId) {
        dentistRepository.deleteById(theId);
    }

    @Override
    public List<Dentist> findAll() {
        return null;
    }

    @Override
    public Dentist findById(int theId) {
        return null;
    }

    @Override
    public void save(Dentist theDentist) {
        dentistRepository.save(theDentist);
    }

    @Override
    public void deleteById(int theId) {

    }
}
