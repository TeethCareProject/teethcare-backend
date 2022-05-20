package com.teethcare.controller;

import com.teethcare.model.entity.Manager;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private CRUDService<Manager> managerService;

    @Autowired
    public ManagerController(CRUDService<Manager> managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<Manager>> getAllManagers() {
        return new ResponseEntity<>(managerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DENTIST', 'CUSTOMER_SERVICE')")
    public ResponseEntity<Optional<Manager>> getManager(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(managerService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity addManager(@Valid @RequestBody Manager manager) {
        Manager addedManager = managerService.save(manager);
        if (addedManager != null) {
            return new ResponseEntity<>(addedManager, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User existed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Manager> delManager(@PathVariable("id") Integer id) {
        Manager deletedManager = managerService.delete(id);
        if (deletedManager != null) {
            return new ResponseEntity<>(deletedManager, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
