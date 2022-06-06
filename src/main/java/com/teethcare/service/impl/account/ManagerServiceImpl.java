package com.teethcare.service.impl.account;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.repository.ManagerRepository;
import com.teethcare.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final RoleService roleService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final ClinicMapper clinicMapper;
    private final LocationService locationService;
    private final ClinicService clinicService;
    private final WardService wardService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public Manager findById(int id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (manager.isEmpty()) {
            throw new NotFoundException("Manager id " + id + " not found!");
        }
        return manager.get();
    }

    @Override
    @Transactional
    public void save(Manager manager) {
        manager.setRole(roleService.getRoleByName(Role.MANAGER.name()));
        manager.setStatus(Status.Account.PENDING.name());
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managerRepository.save(manager);
    }

    @Override
    public void delete(int id) {
        Optional<Manager> managerData = managerRepository.findById(id);
        if (managerData.isPresent()) {
            Manager manager = managerData.get();
            manager.setStatus(Status.Account.INACTIVE.name());
            managerRepository.save(manager);
        } else {
            throw new NotFoundException("Manager id " + id + " not found!");
        }
    }

    @Override
    public void update(Manager theEntity) {

    }

    @Override
    public Manager getActiveManager(int id) {
        return managerRepository.getManagerByIdAndStatusIsNot(id, Status.Account.INACTIVE.name());
    }

    @Override
    public ManagerResponse addNew(ManagerRegisterRequest managerRegisterRequest) {
        managerRegisterRequest.trim();
        boolean isDuplicated = accountService.isDuplicated(managerRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (managerRegisterRequest.getPassword().equals(managerRegisterRequest.getConfirmPassword())) {
                Manager manager = accountMapper.mapManagerRegisterRequestToManager(managerRegisterRequest);
                Clinic clinic = clinicMapper.mapManagerRegisterRequestListToClinic(managerRegisterRequest);
                Location location = new Location();
                location.setWard(wardService.findById(managerRegisterRequest.getWardId()));
                location.setAddressString(managerRegisterRequest.getClinicAddress());
                locationService.save(location);
                this.save(manager);
                clinicService.saveWithManagerAndLocation(clinic, manager, location);
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                return accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse);
            } else {
                throw new BadRequestException("confirm Password is not match with password");
            }
        } else {
            throw new BadRequestException("User existed!");
        }
    }
}
