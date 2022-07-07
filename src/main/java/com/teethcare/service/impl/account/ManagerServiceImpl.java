package com.teethcare.service.impl.account;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.LocationMapper;
import com.teethcare.model.dto.ClinicDTO;
import com.teethcare.model.dto.LocationDTO;
import com.teethcare.model.dto.ManagerDTO;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.repository.ManagerRepository;
import com.teethcare.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final RoleService roleService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final ClinicMapper clinicMapper;
    private final ClinicService clinicService;
    private final PasswordEncoder passwordEncoder;
    private final LocationMapper locationMapper;

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
    @Transactional
    public Clinic addNew(ManagerRegisterRequest managerRegisterRequest) {
        managerRegisterRequest.trim();
        boolean isDuplicated = accountService.isDuplicated(managerRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (managerRegisterRequest.getPassword().equals(managerRegisterRequest.getConfirmPassword())) {
                ManagerDTO managerDTO = accountMapper.mapManagerRegisterRequestToManagerDTO(managerRegisterRequest);
                Manager manager = accountMapper.mapManagerDTOToManager(managerDTO);
                save(manager);

                ClinicDTO clinicDTO = clinicMapper.mapManagerRegisterRequestListToClinic(managerRegisterRequest);
                log.info("Clinic email: " + clinicDTO.getEmail());
                LocationDTO locationDTO = locationMapper.mapManagerRegisterRequestToLocationDTO(managerRegisterRequest);
                Clinic clinic = clinicService.create(clinicDTO, locationDTO, manager);
//                clinic.setManager(manager);
//                clinicService.save(clinic);
//                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
//                accountMapper.mapManagerToManagerResponse(clinic.getManager(), clinicInfoResponse)
                log.info("Save clinic with manager successfully");
//                save(manager);
                return clinic;
            } else {
                throw new BadRequestException("confirm Password is not match with password");
            }
        } else {
            throw new BadRequestException("User existed!");
        }
    }
}
