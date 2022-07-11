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
import com.teethcare.service.*;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Dentist findById(int id) {
        return dentistRepository.findDentistById(id);
    }

    @Override
    public List<Dentist> findAll() {
        return dentistRepository.findAll();
    }


    @Override
    public void save(Dentist dentist) {
        dentist.setStatus(Status.Account.PENDING.name());
        dentist.setRole(roleService.getRoleByName(Role.DENTIST.name()));
        dentist.setPassword(passwordEncoder.encode(dentist.getPassword()));
        dentistRepository.save(dentist);
    }

    @Override
    public void delete(int id) {
        //TODO
    }

    @Override
    public void update(Dentist theEntity) {
        dentistRepository.save(theEntity);
    }

    @Override
    public List<Dentist> findByClinicId(int id) {
        List<Dentist> dentistList = dentistRepository.findByClinicId(id);

        if (dentistList == null || dentistList.size() == 0) {
            throw new NotFoundException("ID not found");
        }

        return dentistList;
    }

    @Override
    public List<Dentist> findByClinicIdAndStatus(int id, String status) {
         return dentistRepository.findByClinicIdAndStatus(id, status);
    }

    @Override
    public Page<Dentist> findDentistByClinicId(int clinicId, Pageable pageable) {
        List<Dentist> dentistList = dentistRepository.findDentistByClinicId(clinicId, pageable);
        if (pageable.isUnpaged()) {
            List<Dentist> unpagedDentistList = dentistRepository.findDentistByClinicId(clinicId, pageable);
            return new PageImpl<>(unpagedDentistList);
        }

        return PaginationAndSortFactory.convertToPage(dentistList, pageable);
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
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            Account account = accountService.getAccountByUsername(username);
            Clinic clinic = clinicService.getClinicByManager(managerService.findById(account.getId()));
            Dentist dentist = accountMapper.mapDentistRegisterRequestToDentist(staffRegisterRequest);
            dentist.setClinic(clinic);
            this.save(dentist);
            return dentist;
        } else {
            throw new BadRequestException("User existed!");
        }
    }

}
