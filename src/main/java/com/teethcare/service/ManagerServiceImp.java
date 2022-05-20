package com.teethcare.service;

import com.teethcare.model.entity.Manager;
import com.teethcare.repository.AccountRepository;
import com.teethcare.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImp implements CRUDService<Manager> {
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public Optional<Manager> findById(Integer id) {
        return managerRepository.findById(id);
    }

    @Override
    public ResponseEntity save(@Valid Manager manager) {
        String username = accountRepository.getActiveUserName(manager.getUsername());
        if (username == null) {
            manager.setId(null);
            manager.setPassword(passwordEncoder.encode(manager.getPassword()));
            return new ResponseEntity<>(managerRepository.save(manager), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User existed!");

    }

    @Override
    public ResponseEntity delete(Integer id) {
        Optional<Manager> managerData = managerRepository.findById(id);
        if (managerData.isPresent()) {
            Manager manager = managerData.get();
            manager.setStatus(0);
            return new ResponseEntity<>(managerRepository.save(manager), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
