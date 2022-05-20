package com.teethcare.controller;

import com.teethcare.model.entity.Manager;
import com.teethcare.service.ManagerServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping("/api/managers")
public class ManagerController {
    @Autowired
    private ManagerServiceImp managerServiceImp;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Manager> getAllManagers() {
        return managerServiceImp.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DENTIST', 'CUSTOMER_SERVICE')")
    public Optional<Manager> getManager(@PathVariable("id") Integer id) {
        return managerServiceImp.findById(id);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity addManager(@Valid @RequestBody Manager patient) {
        return managerServiceImp.save(patient);
    }

    @DeleteMapping ("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Manager> delManager(@PathVariable("id") Integer id) {
        return managerServiceImp.delete(id);
    }
}
