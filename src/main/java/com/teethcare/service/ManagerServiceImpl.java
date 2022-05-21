package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Manager;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements CRUDService<Manager> {

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
    public Optional<Manager> findById(Integer id) {
        return managerRepository.findById(id);
    }

    @Override
    public Manager save(@Valid Manager manager) {
        Account username = accountRepository.getAccountByUsernameAndAndStatusIsNot(manager.getUsername(), 0);
        if (username == null) {
            manager.setId(null);
            manager.setStatus(1);
            manager.setPassword(passwordEncoder.encode(manager.getPassword()));
            return managerRepository.save(manager);
        }
        return null;
    }

    @Override
    public Manager delete(Integer id) {
        Optional<Manager> managerData = managerRepository.findById(id);
        if (managerData.isPresent()) {
            Manager manager = managerData.get();
            manager.setStatus(0);
            return manager;
        }
        return null;
    }
}
