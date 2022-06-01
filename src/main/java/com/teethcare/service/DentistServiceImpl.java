package com.teethcare.service;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Dentist;
import com.teethcare.repository.DentistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {
    private final DentistRepository dentistRepository;
    private final RoleService roleService;
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
        theDentist.setStatus(Status.ACTIVE.name());
        theDentist.setRole(roleService.getRoleByName(Role.DENTIST.name()));
        dentistRepository.save(theDentist);
    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public void update(Dentist theEntity) {
        dentistRepository.save(theEntity);
    }

    @Override
    public List<Dentist> findByClinicId(int theId) {
        List<Dentist> dentistList = dentistRepository.findByClinicId(theId);

        if (dentistList == null || dentistList.size() == 0) {
            throw new NotFoundException("ID not found");
        }

        return dentistList;
    }

    @Override
    public List<Dentist> findByClinicIdAndStatus(int theId, String status) {
        List<Dentist> dentistList = dentistRepository.findByClinicIdAndStatus(theId, status);
        return dentistList;
    }

    @Override
    public Page<Dentist> findAllWithPaging(Pageable pageable) {
        List<Dentist> dentistList = dentistRepository.findAllByStatusIsNotNull(pageable);
        if (dentistList.size() == 0) {
            throw new NotFoundException("Empty List");
        }
        return new PageImpl<>(dentistList);
    }

    @Override
    public Dentist findActiveDentist(int id) {
        return dentistRepository.findDentistByIdAndStatus(id, Status.ACTIVE.name());
    }

}
