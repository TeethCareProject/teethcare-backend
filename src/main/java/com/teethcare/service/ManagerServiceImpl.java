package com.teethcare.service;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Manager;
import com.teethcare.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
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
        manager.setStatus(Status.PENDING.name());
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managerRepository.save(manager);
    }

    @Override
    public void delete(int id) {
        Optional<Manager> managerData = managerRepository.findById(id);
        if (managerData.isPresent()) {
            Manager manager = managerData.get();
            manager.setStatus(Status.INACTIVE.name());
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
        return managerRepository.getManagerByIdAndStatusIsNot(id, Status.INACTIVE.name());
    }
}
