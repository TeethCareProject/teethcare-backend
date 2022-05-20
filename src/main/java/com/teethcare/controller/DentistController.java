package com.teethcare.controller;

import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
//@EnableSwagger2
@RequestMapping("/api/dentists")
public class DentistController {
    private CRUDService<Dentist> dentistService;

    @Autowired
    public DentistController (@Qualifier("dentistServiceImpl") CRUDService<Dentist> dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping()
    public List<Dentist> findAll() {
        return dentistService.findAll();
    }

    @GetMapping("/{id}")
    public Dentist getDentist(@PathVariable int id) {

        Dentist theEmployee = dentistService.findById(id);

        if (theEmployee == null) {
            throw new NotFoundException();
        }

        return theEmployee;
    }

    @DeleteMapping("/{id}")
    public Dentist updateAccountStatus(@PathVariable("id") int id) {
        if (id < 1) {
            throw new NotFoundException();
        }

        Dentist dentist = dentistService.findById(id);

        if (dentist == null) {
            throw new NotFoundException();
        }

        dentist.setId(id);
        dentist.setStatus(false);

        dentistService.save(dentist);

        return dentist;
    }

    @PutMapping
    public Dentist updateCustomer(@RequestBody Dentist dentist) {

        dentistService.save(dentist);

        return dentist;
    }
}
