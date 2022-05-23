package com.teethcare.controller;

import com.teethcare.config.MapStructMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@EnableSwagger2
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/dentists")
public class DentistController {
    private CRUDService<Dentist> dentistService;
    private MapStructMapper mapstructMapper;

    @Autowired
    public DentistController (@Qualifier("dentistServiceImpl") CRUDService<Dentist> dentistService,
                                MapStructMapper mapstructMapper) {
        this.mapstructMapper = mapstructMapper;
        this.dentistService = dentistService;
    }

    @GetMapping()
    public List<Dentist> findAll() {
        return dentistService.findAll();
    }

    @GetMapping("/{id}")
    public DentistResponse getDentist(@PathVariable int id) {

        Dentist theDentist = dentistService.findById(id);

        if (theDentist == null) {
            throw new NotFoundException();
        }

        DentistResponse dentistResponse = new DentistResponse();
        dentistResponse = mapstructMapper.mapDentistToDentistResponse(theDentist);
        return dentistResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateAccountStatus(@PathVariable("id") int id) {
        if (id < 1) {
            throw new NotFoundException();
        }

        Dentist dentist = dentistService.findById(id);

        if (dentist == null) {
            throw new NotFoundException();
        }

        dentist.setId(id);
        dentist.setStatus("INACTIVE");

        dentistService.save(dentist);

        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }
}
