package com.teethcare.service;

import com.teethcare.common.Status;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Manager;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepository;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public Manager findById(int id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (manager.isEmpty()) {
            throw new IdNotFoundException();
        }
        return manager.get();
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public void delete(int id) {
        Optional<Manager> managerData = managerRepository.findById(id);
        Manager manager = managerData.get();
        manager.setStatus(Status.INACTIVE.name());
        managerRepository.save(manager);
    }

    @Override
    public Manager getActiveManager(int id) {
        return managerRepository.getManagerByIdAndStatusIsNot(id, Status.INACTIVE.name());
    }
}
