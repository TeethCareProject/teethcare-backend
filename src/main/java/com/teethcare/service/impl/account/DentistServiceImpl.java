package com.teethcare.service.impl.account;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.request.StaffRegisterRequest;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.DentistRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.DentistService;
import com.teethcare.service.ManagerService;
import com.teethcare.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {
    private final DentistRepository dentistRepository;
    private final RoleService roleService;
    private final ClinicRepository clinicService;
    private final ManagerService managerService;
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Dentist findById(int theId) {
        Optional<Dentist> result = dentistRepository.findById(theId);

        Dentist theDentist;

        if (result.isPresent()) {
            theDentist = result.get();
        } else {
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
        theDentist.setStatus(Status.Account.ACTIVE.name());
        theDentist.setRole(roleService.getRoleByName(Role.DENTIST.name()));
        theDentist.setPassword(passwordEncoder.encode(theDentist.getPassword()));
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
        return dentistRepository.findByClinicIdAndStatus(theId, status);
    }

    @Override
    public Page<Dentist> findAllWithPaging(Pageable pageable) {
        List<Dentist> dentistList = dentistRepository.findAllByStatusIsNotNull(pageable);
        return new PageImpl<>(dentistList);
    }

    @Override
    public Dentist findActive(int id) {
        return dentistRepository.findDentistByIdAndStatus(id, Status.Account.ACTIVE.name());
    }

    @Override
    public Dentist addNew(StaffRegisterRequest staffRegisterRequest, String token) {
        staffRegisterRequest.trim();
        boolean isDuplicated = accountService.isDuplicated(staffRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (staffRegisterRequest.getPassword().equals(staffRegisterRequest.getConfirmPassword())) {
                String username = jwtTokenUtil.getUsernameFromJwt(token);
                Account account = accountService.getAccountByUsername(username);
                Clinic clinic = clinicService.getClinicByManager(managerService.findById(account.getId()));

                Dentist dentist = accountMapper.mapDentistRegisterRequestToDentist(staffRegisterRequest);
                dentist.setClinic(clinic);
                this.save(dentist);
                return dentist;
            } else {
                throw new BadRequestException("confirm Password is not match with password");
            }
        } else {
            throw new BadRequestException("User existed!");
        }
    }

}
